# インスタンスのための変数
variable "instance_shape" {}
variable "ssh_public_key" {}
variable "ssh_private_key" {}
variable "storage_size" {}

# リージョン毎のOracle Linux 7.6 のイメージの OCID
variable "instance_image_ocid" {
  type = "map"
  default = {
    // 各イメージのOCIDは https://docs.us-phoenix-1.oraclecloud.com/images/ を参照下さい
    // 今回は Oracle が提供している "Oracle-Linux-7.6-2018.11.19-0" イメージを使用します
    eu-frankfurt-1 = "ocid1.image.oc1.eu-frankfurt-1.aaaaaaaa2rvnnmdz6ewn4pozatb2l6sjtpqpbgiqrilfh3b4ee7salrwy3kq"
    us-ashburn-1 = "ocid1.image.oc1.iad.aaaaaaaa2mnepqp7wn3ej2axm2nkoxwwcdwf7uc246tcltg4li67z6mktdiq"
    uk-london-1 = "ocid1.image.oc1.uk-london-1.aaaaaaaaikjrglbnzkvlkiltzobfvtxmqctoho3tmdcwopnqnoolmwbsk3za"
    us-phoenix-1 = "ocid1.image.oc1.phx.aaaaaaaaaujbtv32uv4mizzbgnjkjlvbeaiqj5sgc6r5umfunebt7qpzdzmq"
  }
}

# 事前準備

resource "oci_core_instance" "prepare_instance" {
  display_name        = "事前準備用のインスタンス"
  availability_domain = "${oci_core_subnet.ad_subnet.availability_domain}"
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  shape = "VM.Standard2.1"
  fault_domain = "FAULT-DOMAIN-1"
  create_vnic_details {
    subnet_id = "${oci_core_subnet.ad_subnet.id}"
  }
  source_details {
    source_type = "image"
    source_id = "${var.instance_image_ocid[var.region]}"
  }
  metadata {
    ssh_authorized_keys = "${var.ssh_public_key}"
  }
}

resource "oci_core_volume" "prepare_volume" {
  display_name        = "事前準備しているストレージ"
  availability_domain = "${oci_core_subnet.ad_subnet.availability_domain}"
  compartment_id      = "${oci_identity_compartment.target_compartment.id}"
  size_in_gbs         = "${var.storage_size}"
}

resource "oci_core_volume_attachment" "prepare_volume_attach" {
  display_name        = "事前準備しているストレージ"
  compartment_id  = "${oci_identity_compartment.target_compartment.id}"
  instance_id     = "${oci_core_instance.prepare_instance.id}"
  volume_id       = "${oci_core_volume.prepare_volume.id}"
  attachment_type = "iscsi"
}


resource "null_resource" "preparing_task" {
  depends_on = ["oci_core_instance.prepare_instance", "oci_core_volume_attachment.prepare_volume_attach"]
  connection {
    agent       = false
    timeout     = "300m"
    host        = "${oci_core_instance.prepare_instance.public_ip}"
    user        = "opc"
    private_key = "${file("${var.ssh_private_key}")}"
  }
  provisioner "file" {
    source = "script"
    destination = "/home/opc/"
  }
  provisioner "remote-exec" {
    inline = [ <<EOS
sh /home/opc/script/initVolume.sh ${oci_core_volume_attachment.prepare_volume_attach.iqn} ${oci_core_volume_attachment.prepare_volume_attach.ipv4} ${oci_core_volume_attachment.prepare_volume_attach.port} /src
sh /home/opc/script/prepareForOpenJDK.sh /src
EOS
    ]
  }
}


# 事前準備した boot ボリュームをコピー

resource "oci_core_boot_volume" "copied_boot_volume" {
  depends_on = ["null_resource.preparing_task"]
  availability_domain = "${oci_core_instance.prepare_instance.availability_domain}"
  compartment_id      = "${oci_core_instance.prepare_instance.compartment_id}"
  source_details {
    id   = "${oci_core_instance.prepare_instance.boot_volume_id}"
    type = "bootVolume"
  }
}



# ビルド&テスト用インスタンスを作成

resource "oci_core_instance" "worker_instance" {
  display_name        = "Worker for building and testing OpenJDK"
  depends_on = ["oci_core_boot_volume.copied_boot_volume"]
  availability_domain = "${oci_core_subnet.ad_subnet.availability_domain}"
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  shape = "${var.instance_shape}"
  fault_domain = "FAULT-DOMAIN-1"
  create_vnic_details {
    subnet_id = "${oci_core_subnet.ad_subnet.id}"
    assign_public_ip = true
  }
  source_details {
    source_id = "${oci_core_boot_volume.copied_boot_volume.id}"
    source_type = "bootVolume"
  }
  metadata {
    ssh_authorized_keys = "${var.ssh_public_key}"
  }
}

resource "oci_core_volume" "worker_volume" {
  display_name        = "Work volume for Worker"
  depends_on = ["oci_core_boot_volume.copied_boot_volume"]
  availability_domain = "${oci_core_subnet.ad_subnet.availability_domain}"
  compartment_id      = "${oci_identity_compartment.target_compartment.id}"
  source_details {
    id = "${oci_core_volume.prepare_volume.id}"
    type = "volume"
  }
}

resource "oci_core_volume_attachment" "worker_volume_attach" {
  display_name        = "Work volume ttachment for Worker"
  compartment_id  = "${oci_identity_compartment.target_compartment.id}"
  instance_id     = "${oci_core_instance.worker_instance.id}"
  volume_id       = "${oci_core_volume.worker_volume.id}"
  attachment_type = "iscsi"
}

resource "null_resource" "build_and_test_task" {
  depends_on = ["oci_core_instance.worker_instance", "oci_core_volume_attachment.worker_volume_attach"]
  connection {
    agent       = false
    timeout     = "120m"
    host        = "${oci_core_instance.worker_instance.public_ip}"
    user        = "opc"
    private_key = "${file("${var.ssh_private_key}")}"
  }
  provisioner "file" {
    source = "script"
    destination = "/home/opc/"
  }
  provisioner "file" {
    source = "userdata/myPatch"
    destination = "/home/opc/"
  }
  provisioner "remote-exec" {
    inline = [ <<EOS
sh /home/opc/script/initialization.sh ${oci_core_volume_attachment.worker_volume_attach.iqn} ${oci_core_volume_attachment.worker_volume_attach.ipv4} ${oci_core_volume_attachment.worker_volume_attach.port} /src
EOS
    ]
  }
  provisioner "remote-exec" {
    inline = [ <<EOS
sh /home/opc/script/buildOpenJDK.sh /src
sh /home/opc/script/testOpenJDK.sh /src
EOS
    ]
  }
}
# インスタンスのための変数
variable "instance_shape" {}
variable "ssh_public_key" {}

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

resource "oci_core_instance" "instance1" {
  count = "1"
  availability_domain = "${oci_core_subnet.ad_subnet.availability_domain}"
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  shape = "${var.instance_shape}"
  fault_domain = "FAULT-DOMAIN-1"
  create_vnic_details {
    subnet_id = "${oci_core_subnet.ad_subnet.id}"
    assign_public_ip = true
  }
  source_details {
    source_type = "image"
    source_id = "${var.instance_image_ocid[var.region]}"
  }
  metadata {
    ssh_authorized_keys = "${var.ssh_public_key}"
  }
}
# ネットワークなどの基盤のための変数
variable "parrent_compartment_ocid" {}
variable "environment_name" {}
variable "environment_description" {}
variable "domain_name" {}

# 既存のコンパートメントの下に新しいコンパートメントを作成
resource "oci_identity_compartment" "target_compartment" {
  name = "${var.environment_name}"
  description = "${var.environment_description}"
  compartment_id = "${var.parrent_compartment_ocid}"
}

# 仮想クラウドネットワーク
resource "oci_core_vcn" "target_vcn" {
  cidr_block = "10.0.0.0/16"
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  dns_label = "${var.environment_name}"
}

# インターネットゲートウェイ
resource "oci_core_internet_gateway" "ig" {
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  vcn_id = "${oci_core_vcn.target_vcn.id}"
}

# デフォルトルートテーブルを修正
resource "oci_core_default_route_table" "default-route-table" {
  manage_default_resource_id = "${oci_core_vcn.target_vcn.default_route_table_id}"

  route_rules {
    destination = "0.0.0.0/0"
    destination_type = "CIDR_BLOCK"
    network_entity_id = "${oci_core_internet_gateway.ig.id}"
  }
}

# デフォルトDHCPオプションを修正
resource "oci_core_default_dhcp_options" "default-dhcp-options" {
  manage_default_resource_id = "${oci_core_vcn.target_vcn.default_dhcp_options_id}"

  options {
    type = "DomainNameServer"
    server_type = "VcnLocalPlusInternet"
  }
  options {
    type = "SearchDomain"
    search_domain_names = [
      "${var.domain_name}"]
  }
}

# デフォルトセキュリティリストを修正
resource "oci_core_default_security_list" "default-security-list" {
  manage_default_resource_id = "${oci_core_vcn.target_vcn.default_security_list_id}"

  egress_security_rules {
    destination = "0.0.0.0/0"
    protocol = "6"
    tcp_options {
      "min" = 80
      "max" = 80
    }
  }

  egress_security_rules {
    destination = "0.0.0.0/0"
    protocol = "6"
    tcp_options {
      "min" = 443
      "max" = 443
    }
  }

  ingress_security_rules {
    protocol = "6"
    source = "0.0.0.0/0"
    tcp_options {
      "min" = 22
      "max" = 22
    }
  }

  ingress_security_rules {
    protocol = 1
    source = "0.0.0.0/0"
    stateless = true
    icmp_options {
      "type" = 3
      "code" = 4
    }
  }
}

# AD1にサブネットを作る
resource "oci_core_subnet" "ad_subnet" {
  compartment_id = "${oci_identity_compartment.target_compartment.id}"
  vcn_id = "${oci_core_vcn.target_vcn.id}"
  availability_domain = "${lookup(data.oci_identity_availability_domains.ads.availability_domains[0], "name")}"
  dns_label = "ad1"
  cidr_block = "10.0.1.0/24"
}

# 利用可能な Availability Domain の一覧を取得する
data "oci_identity_availability_domains" "ads" {
  compartment_id = "${var.parrent_compartment_ocid}"
}
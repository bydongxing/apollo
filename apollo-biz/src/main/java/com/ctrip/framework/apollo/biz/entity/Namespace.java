package com.ctrip.framework.apollo.biz.entity;

import com.ctrip.framework.apollo.common.entity.BaseEntity;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Namespace")
@SQLDelete(sql = "Update Namespace set isDeleted = 1 where id = ?")
@Where(clause = "isDeleted = 0")
public class Namespace extends BaseEntity {

  /**
   * App 编号 {@link com.ctrip.framework.apollo.common.entity.App#appId}
   */
  @Column(name = "appId", nullable = false)
  private String appId;

  /**
   * Cluster 名 {@link Cluster#name}
   */
  @Column(name = "ClusterName", nullable = false)
  private String clusterName;

  /**
   * AppNamespace 名 {@link com.ctrip.framework.apollo.common.entity.AppNamespace#name}
   */
  @Column(name = "NamespaceName", nullable = false)
  private String namespaceName;

  public Namespace(){

  }

  public Namespace(String appId, String clusterName, String namespaceName) {
    this.appId = appId;
    this.clusterName = clusterName;
    this.namespaceName = namespaceName;
  }

  public String getAppId() {
    return appId;
  }

  public String getClusterName() {
    return clusterName;
  }

  public String getNamespaceName() {
    return namespaceName;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public void setNamespaceName(String namespaceName) {
    this.namespaceName = namespaceName;
  }

  public String toString() {
    return toStringHelper().add("appId", appId).add("clusterName", clusterName)
        .add("namespaceName", namespaceName).toString();
  }
}

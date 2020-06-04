/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.coresystems.models;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oratile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "products")
@NamedQueries(
        {
                @NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p")
                , @NamedQuery(name = "Products.findByProductId", query = "SELECT p FROM Products p WHERE p.productId = :productId")
                , @NamedQuery(name = "Products.findByProductName", query = "SELECT p FROM Products p WHERE p.productName = :productName")
                , @NamedQuery(name = "Products.findByAmount", query = "SELECT p FROM Products p WHERE p.amount = :amount")
        })
public class Products implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "product_id")
  @Expose
  private Integer productId;
  @Expose
  @Basic(optional = false)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "product_name")
  private String productName;
  // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
  @Expose
  @Column(name = "amount")
  private Double amount;

  @ManyToMany(mappedBy = "p")
  private List<Payments> payments = new ArrayList<>();


  public Products(Integer productId) {
    this.productId = productId;
  }

  public Products(String product_name, Double amount2) {
    this.productName = product_name;
    this.amount = amount2;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (productId != null ? productId.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Products)) {
      return false;
    }
    Products other = (Products) object;
    if ((this.productId == null && other.productId != null) || (this.productId != null && !this.productId.equals(other.productId))) {
      return false;
    }
    return true;
  }

}

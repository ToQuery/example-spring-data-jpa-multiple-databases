package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tb_pay")
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pay_no")
    private String payNo;

    @Column(name = "pay_date")
    private LocalDateTime payDate;


    @Column(name = "update_date")
    private LocalDateTime updateDate;
}

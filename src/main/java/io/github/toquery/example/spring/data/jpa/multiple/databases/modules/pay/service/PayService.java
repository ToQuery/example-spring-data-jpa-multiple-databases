package io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.service;

import io.github.toquery.example.spring.data.jpa.multiple.databases.ExampleSpringDataJpaMultipleDatabasesApplication;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.dao.PayRepository;
import io.github.toquery.example.spring.data.jpa.multiple.databases.modules.pay.entity.Pay;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 */
@RequiredArgsConstructor
@Transactional(transactionManager = "payTransactionManager")
@Service
public class PayService {
    private final PayRepository payRepository;

    public Pay init() {
        Pay pay = new Pay();
        pay.setId(1);
        pay.setPayNo("test");
        pay.setPayDate(LocalDateTime.now());
        pay.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        payRepository.saveAndFlush(pay);
        return pay;
    }
    public Pay save() {
        Pay pay = new Pay();
        pay.setPayNo("test");
        pay.setPayDate(LocalDateTime.now());
        pay.setUpdateDate(ExampleSpringDataJpaMultipleDatabasesApplication.INIT_DATE);
        return this.save(pay);
    }

    public Pay save(Pay pay) {
        return payRepository.save(pay);
    }

    public Pay findById(Integer id) {
        return payRepository.findById(id).orElse(null);
    }

    public void delete() {
        payRepository.deleteAll();
    }
    public void delete(Pay pay) {
        payRepository.delete(pay);
    }

    public List<Pay> list() {
        return payRepository.findAll();
    }

    public List<Pay> update() {
        List<Pay> pays = this.list();
        pays.forEach(pay -> pay.setUpdateDate(LocalDateTime.now()));
        return payRepository.saveAllAndFlush(pays);
    }
}

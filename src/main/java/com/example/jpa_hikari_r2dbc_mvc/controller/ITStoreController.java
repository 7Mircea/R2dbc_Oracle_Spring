package com.example.jpa_hikari_r2dbc_mvc.controller;

import com.example.jpa_hikari_r2dbc_mvc.model.Produse;
import com.example.jpa_hikari_r2dbc_mvc.repository.ITStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/itStore")
public class ITStoreController {

    @Autowired
    ITStoreRepository repository;

    @GetMapping("/produse")
    public List<Produse> getAllProduse() {
        return repository.findAll().subscribeOn(Schedulers.boundedElastic()).buffer().blockLast();
    }

    @GetMapping("/{id}")
    public Optional<Produse> getProdusById(@PathVariable Integer id) {
        return repository.findById(id).subscribeOn(Schedulers.boundedElastic()).blockOptional();
    }

}

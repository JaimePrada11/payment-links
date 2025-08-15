package com.laura.paymentlinks.paymentlinks.model;


import com.laura.paymentlinks.paymentlinks.dto.MerchantDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Merchant {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Email
    @Column(unique = true, nullable = false)
    private String email;
    private String password;

    @OneToMany(mappedBy = "merchant", cascade = CascadeType.ALL)
    private List<PaymentLink> paymentLinks;

    public Merchant(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.paymentLinks = new ArrayList<>();
    }

    public void addPaymentLink(PaymentLink link) {
        paymentLinks.add(link);
        link.setMerchant(this);
    }

    public void removePaymentLink(PaymentLink link) {
        paymentLinks.remove(link);
        link.setMerchant(null);
    }

    public MerchantDto toDto() {
        name = this.name;
        email = this.email;

        return new MerchantDto( name, email);
    }

    public Merchant fromDto(MerchantDto dto) {
        Merchant merchant = new Merchant();
        merchant.setName(dto.name());
        merchant.setEmail(dto.email());

        return merchant;
    }
}

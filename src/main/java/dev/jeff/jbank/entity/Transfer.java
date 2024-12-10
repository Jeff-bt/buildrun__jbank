package dev.jeff.jbank.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_transfers")
public class Transfer {

    @Id
    @Column(name = "tranfer_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID transferId;

    @ManyToOne()
    @JoinColumn(name = "wallet_receiver_id")
    private Wallet receiver;
    @ManyToOne()
    @JoinColumn(name = "wallet_sender_id")
    private Wallet sender;
    @Column(name = "transfer_value")
    private BigDecimal transferValue;
    @Column(name = "transfer_date_time")
    private LocalDateTime transferDateTime;
}


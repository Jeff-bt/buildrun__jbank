package dev.jeff.jbank.repository;

import dev.jeff.jbank.entity.Wallet;
import dev.jeff.jbank.repository.dto.StatementView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    String SQL_STATEMENT =
            """
                SELECT
                    BIN_TO_UUID(transfer_id) as statement_id,
                    "transfer" as type,
                    transfer_value as statement_value,
                    BIN_TO_UUID(wallet_receiver_id) as wallet_receiver,
                    BIN_TO_UUID(wallet_sender_id) as wallet_sender,
                    transfer_date_time as statement_date_time
                FROM tb_transfers
                WHERE wallet_receiver_id = UUID_TO_BIN(?1) OR wallet_sender_id = UUID_TO_BIN(?1)
                UNION ALL
                SELECT
                    BIN_TO_UUID(deposit_id) as statement_id,
                    "deposit" as type,
                    deposit_value as deposit_value,
                    BIN_TO_UUID(wallet_id) as wallet_receiver,
                    "" as wallet_sender,
                    deposit_date_time as statement_date_time
                FROM tb_deposit
                WHERE wallet_id = UUID_TO_BIN(?1)
            """;

    String SQL_COUNT_STATEMENT =
            """
                SELECT COUNT(*) FROM
                (
                """ + SQL_STATEMENT + """
                ) as total
            """;

    Boolean existsByCpfOrEmailAllIgnoreCase(String cpf, String email);

    @Query(nativeQuery = true, value = SQL_STATEMENT, countQuery = SQL_COUNT_STATEMENT)
    Page<StatementView> findStatements(String walletId, PageRequest pageRequest);
}

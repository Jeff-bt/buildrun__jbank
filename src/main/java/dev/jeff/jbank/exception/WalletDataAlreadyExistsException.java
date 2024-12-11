package dev.jeff.jbank.exception;

import org.springframework.http.ProblemDetail;

public class WalletDataAlreadyExistsException extends JBankException {

    private String detail;

    public WalletDataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("JBank Internal Server Error");
        pd.setDetail(detail);

        return pd;
    }
}

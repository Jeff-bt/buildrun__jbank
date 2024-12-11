package dev.jeff.jbank.exception;

import org.springframework.http.ProblemDetail;

public class DeleteWalletException extends JBankException {

    private String detail;

    public DeleteWalletException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail toProblemDetail() {
        var pd = ProblemDetail.forStatus(422);

        pd.setTitle("You cannot delete this walllet");
        pd.setDetail(detail);

        return pd;
    }
}

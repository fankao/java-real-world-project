package com.java.practice;

import java.time.Month;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class BankStatementProcessor {
    private final List<BankTransaction> bankTransactions;

    public BankStatementProcessor(List<BankTransaction> bankTransactions) {
        this.bankTransactions = bankTransactions;
    }

    public SummaryStatistics summaryStatistics() {
        final DoubleSummaryStatistics doubleSummaryStatistics = bankTransactions.stream()
                .mapToDouble(BankTransaction::getAmount)
                .summaryStatistics();
        return new SummaryStatistics(
                doubleSummaryStatistics.getSum(),
                doubleSummaryStatistics.getMax(),
                doubleSummaryStatistics.getMin(),
                doubleSummaryStatistics.getAverage());
    }

    public double summarizeTransactions(final BankTransactionSummarizer
                                                bankTransactionSummarizer) {
        double result = 0;
        for (final BankTransaction bankTransaction : bankTransactions) {
            result = bankTransactionSummarizer.summarize(result,
                    bankTransaction);
        }
        return result;
    }

    public double calculateTotalAmount() {
        return summarizeTransactions((accumulator, bankTransaction) -> accumulator + bankTransaction.getAmount());
    }

    public double calculateTotalInMonth(final Month month) {
        return summarizeTransactions((accumulator, bankTransaction) ->
                bankTransaction.getDate().getMonth() == month ? accumulator + bankTransaction.getAmount() : accumulator);

    }

    public double calculateTotalForCategory(final String category) {
        return summarizeTransactions((accumulator, bankTransaction) ->
                bankTransaction.getDescription().equals(category) ? accumulator + bankTransaction.getAmount() : accumulator);
    }

    public List<BankTransaction> findTransactions(final BankTransactionFilter
                                                          bankTransactionFilter) {
        final List<BankTransaction> result = new ArrayList<>();
        for (final BankTransaction bankTransaction : bankTransactions) {
            if (bankTransactionFilter.test(bankTransaction)) {
                result.add(bankTransaction);
            }
        }
        return result;
    }

    public List<BankTransaction> findTransactionsGreaterThanEqual(final int amount) {
        return findTransactions(bankTransaction ->
                bankTransaction.getAmount() >= amount);
    }
}

package com.java.practice;

public interface BankTransactionSummarizer {
    double summarize(double accumulator, BankTransaction bankTransaction);
}

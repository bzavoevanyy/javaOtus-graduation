package com.bzavoevanyy.service;

public class DataProcessorImpl implements DataProcessor {

    final static String PRIMARY_SCHOOL = "Отменяют  занятия для учащихся с первого по четвертый классы";
    final static String MIDDLE_SCHOOL = "Отменяют  занятия для учащихся с первого по восьмой классы";
    final static String HIGH_SCHOOL = "Отменяют  занятия для учащихся с первого по одиннадцатый классы";
    final static String GOOD_WHETHER = "Температурные условия не превышают пороговых для объявления актировки";

    @Override
    public String process(int temp, int wind) {

        if (wind < 1) {
            if (temp <= -36) {
                return HIGH_SCHOOL;
            }
            if (temp <= -32) {
                return MIDDLE_SCHOOL;
            }
            if (temp <= -29) {
                return PRIMARY_SCHOOL;
            }
        }
        if (wind < 5) {
            if (temp <= -34) {
                return HIGH_SCHOOL;
            }
            if (temp <= -30) {
                return MIDDLE_SCHOOL;
            }
            if (temp < -27) {
                return PRIMARY_SCHOOL;
            }
        }
        if (wind <= 10) {
            if (temp <= -32) {
                return HIGH_SCHOOL;
            }
            if (temp <= -28) {
                return MIDDLE_SCHOOL;
            }
            if (temp < -25) {
                return PRIMARY_SCHOOL;
            }
        }
        if (wind > 10) {
            if (temp <= -31) {
                return HIGH_SCHOOL;
            }
            if (temp <= -27) {
                return MIDDLE_SCHOOL;
            }
            if (temp < -24) {
                return PRIMARY_SCHOOL;
            }
        }
        return GOOD_WHETHER;
    }

}

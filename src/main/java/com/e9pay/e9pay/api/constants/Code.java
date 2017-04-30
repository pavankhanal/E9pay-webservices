package com.e9pay.e9pay.api.constants;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
public enum Code {

    FIXED_RATE_COMMISION_POLICY("COMISSION", "FIXED_RATE"),
    FIXED_AMOUNT_COMMISSION_POLICY("COMISSION", "FIXED_AMOUNT"),
    BRANCH_HQ("BRACH", "HQ"),
    USER_GROUP_SUPER_ADMIN("USER_GROUP", "SUPER_AMDIN");

    private final String classCode;
    private final String value;

    Code(String classCode, String value) {
        this.classCode = classCode;
        this.value = value;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name();
    }

    public Code getByClassAndCode(String classCode, String codeValue) {

        final Code[] values = values();
        for (Code value : values) {
            if (value.getClassCode().equals(classCode) && value.getValue().equals(codeValue)) {
                return value;
            }
        }
        throw new RuntimeException("Unknown Code.");
    }

}

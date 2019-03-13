package com.fs.something.repository.pojo;

/**
 * Created by FS on 2018/5/10.
 */
public enum BusinessType {
    IMPORT {
        public String getInfo() {
            return "进口";
        }

        public String getCode() {
            return "1";
        }

        public String getOrderType() {
            return "FA";
        }
    }, EXPORT {
        public String getInfo() {
            return "出口";
        }

        public String getCode() {
            return "2";
        }

        public String getOrderType() {
            return "FB";
        }
    }, HELP_PURCHASE {
        public String getInfo() {
            return "代采";
        }

        public String getCode() {
            return "3";
        }

        public String getOrderType() {
            return "FC";
        }
    }, VIRTUAL_PRODUCT {
        public String getInfo() {
            return "虚拟生产";
        }

        public String getCode() {
            return "5";
        }

        public String getOrderType() {
            return "FE";
        }
    };

    /**
     * 定义抽象方法
     *
     * @return
     */
    public abstract String getInfo();

    /**
     * 定义抽象方法
     *
     * @return
     */
    public abstract String getCode();

    /**
     * 定义抽象方法
     *
     * @return
     */
    public abstract String getOrderType();

    public static BusinessType getBusinessType(String code) {
        BusinessType result = IMPORT;
        switch (code) {
            case "1":
                result = IMPORT;
                break;
            case "2":
                result = EXPORT;
                break;
            case "3":
                result = HELP_PURCHASE;
                break;
            case "5":
                result = VIRTUAL_PRODUCT;
                break;
            default:
                break;

        }
        return result;
    }
}

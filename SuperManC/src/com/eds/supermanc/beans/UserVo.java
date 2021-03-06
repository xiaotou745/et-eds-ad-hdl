package com.eds.supermanc.beans;

import java.util.ArrayList;
import java.util.List;

public class UserVo {

    private int Status;
    private String Message;
    private User Result;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public User getResult() {
        return Result;
    }

    public void setResult(User result) {
        Result = result;
    }

    @Override
    public String toString() {
        return "UserVo [Status=" + Status + ", Message=" + Message + ", Result=" + Result + "]";
    }

    public class User {
        private String userId;
        private String userName;
        private String password;
        private int status;// 0未通过 1通过 2审核未通过
        private String Amount;
        private String city;
        private String cityId;
        private List<String> BusibussId;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getAmount() {
            return Amount;
        }

        public void setAmount(String amount) {
            Amount = amount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCity() {
            return city;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public List<String> getBusibussId() {
            if (BusibussId == null) {
                BusibussId = new ArrayList<String>();
            }
            return BusibussId;
        }

        public void setBusibussId(List<String> busibussId) {
            BusibussId = busibussId;
        }

        public String getBusibussInfo() {
            StringBuffer sb = new StringBuffer();
            sb.append("BusibussId:[");
            if (getBusibussId().size() <= 0) {
                sb.append("]");
                return sb.toString();
            }
            for (String bid : BusibussId) {
                sb.append(bid).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            return sb.toString();
        }

        @Override
        public String toString() {
            return "User [userId=" + userId + ", userName=" + userName + ", status=" + status + ", Amount=" + Amount
                    + ",city=" + city + ",cityId=" + cityId + "BusibussId=" + getBusibussInfo() + "]";
        }
    }

}

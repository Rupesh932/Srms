package com.srms.model.tenant;

import com.srms.enums.IdentityType;
import com.srms.enums.TenantStatus;
import com.srms.model.base.SrmsEntity;

public class Tenant extends SrmsEntity {
    private final String name;
    private final String phoneNo;
    private final String identityNo;
    private final IdentityType identityType;
    private final int totalFamilyMember;
    private final int bikeCount;
    private final int carCount;
    private final String referredBy;
    private final String profilePicPath;
    private final String identityDocPath;
    private final TenantStatus status;
    private final boolean isPoliceVerified;

    private Tenant(Builder builder){
        super(builder.id);
        this.name = builder.name;
        this.phoneNo = builder.phoneNo;
        this.identityNo = builder.identityNo;
        this.identityType = builder.identityType;
        this.totalFamilyMember = builder.totalFamilyMember;
        this.bikeCount = builder.bikeCount;
        this.carCount = builder.carCount;
        this.referredBy = builder.referredBy;
        this.profilePicPath = builder.profilePicPath;
        this.identityDocPath = builder.identityDocPath;
        this.status  = builder.status;
        this.isPoliceVerified = builder.isPoliceVerified;


    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public IdentityType getIdentityType() {
        return identityType;
    }

    public int getTotalFamilyMember() {
        return totalFamilyMember;
    }

    public int getBikeCount() {
        return bikeCount;
    }

    public int getCarCount() {
        return carCount;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public String getIdentityDocPath() {
        return identityDocPath;
    }

    public TenantStatus getStatus() {
        return status;
    }

    public boolean isPoliceVerified() {
        return isPoliceVerified;
    }

    public static class Builder{
        private final String id;
        private final String name;
        private final String phoneNo;

        private String identityNo = "N/A";
        private IdentityType identityType = IdentityType.OTHER;
        private int totalFamilyMember = 1;
        private int bikeCount = 0;
        private int carCount = 0;
        private String referredBy = "Direct";
        private String profilePicPath = "default-profile.png";
        private String identityDocPath = "none.pdf";
        private TenantStatus status = TenantStatus.ACTIVE;
        private boolean isPoliceVerified = false;

        public Builder(String id,String name,String phoneNo){
            this.id = id;
            this.name = name;
            this.phoneNo = phoneNo;
        }

        public Builder identityNo(String identityNo){
            this.identityNo = identityNo;
            return this;
        }
        public Builder identityType(IdentityType identityType){
            this.identityType = identityType;
            return this;
        }
        public Builder totalFamilyMember(int totalFamilyMember) {
            this.totalFamilyMember = totalFamilyMember;
            return this;
        }

        public Builder bikeCount(int bikeCount) {
            this.bikeCount = bikeCount;
            return this;
        }

        public Builder carCount(int carCount) {
            this.carCount = carCount;
            return this;
        }

        public Builder referredBy(String referredBy) {
            this.referredBy = referredBy;
            return this;
        }

        public Builder profilePicPath(String profilePicPath) {
            this.profilePicPath = profilePicPath;
            return this;
        }

        public Builder identityDocPath(String identityDocPath) {
            this.identityDocPath = identityDocPath;
            return this;
        }

        public Builder status(TenantStatus status) {
            this.status = status;
            return this;
        }

        public Builder isPoliceVerified(boolean isPoliceVerified) {
            this.isPoliceVerified = isPoliceVerified;
            return this;
        }

        public Tenant build(){
            return new Tenant(this);
        }
    }

    @Override
    public String toString() {
        return "Tenant{" +
                super.toString() + // SrmsEntity को ID, createdAt, updatedAt आउँछ
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", identityNo='" + identityNo + '\'' +
                ", identityType=" + identityType +
                ", totalFamilyMember=" + totalFamilyMember +
                ", bikeCount=" + bikeCount +
                ", carCount=" + carCount +
                ", referredBy='" + referredBy + '\'' +
                ", profilePicPath='" + profilePicPath + '\'' +
                ", identityDocPath='" + identityDocPath + '\'' +
                ", status=" + status +
                ", isPoliceVerified=" + isPoliceVerified +
                '}';
    }
}

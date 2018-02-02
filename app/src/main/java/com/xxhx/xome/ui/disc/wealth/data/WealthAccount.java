package com.xxhx.xome.ui.disc.wealth.data;

import android.text.TextUtils;
import com.xxhx.xome.util.StringUtil;
import java.text.DecimalFormat;
import java.util.Date;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by xxhx on 2017/4/4.
 */

@Entity(generateConstructors = false)
public class WealthAccount {
    @Id
    private Long id;
    @Convert(converter = AccountTypeConverter.class, columnType = String.class)
    private AccountType type;
    @Convert(converter = OrganizationConverter.class, columnType = String.class)
    private Organization organization;
    private String no;
    private String alias;
    private Date addedTime;
    private Date updatedTime;
    private long balanceInFens;
    public WealthAccount() {
    }
    @Keep
    public String getDisplayName() {
        if(!TextUtils.isEmpty(alias)) {
            if(type == AccountType.INTERNET) {
                return alias;
            }
            else {
                return alias + "-" + no.substring(no.length() - 4);
            }
        }
        if(type == AccountType.DEBIT) {
            return String.format("[%1s] %2s", organization.getName(), StringUtil.getMaskedString(no));
        }
        if(type == AccountType.CREDIT) {
            return String.format("[%1s] %2s", organization.getName(), StringUtil.getMaskedString(no));
        }
        if(type == AccountType.INTERNET) {
            return organization.getName();
        }
        return no;
    }
    @Keep
    public String getFormattedBalance() {
        DecimalFormat format = new DecimalFormat("#,####");
        return String.format("%s.%02d", format.format(balanceInFens / 100), Math.abs(balanceInFens) % 100);
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AccountType getType() {
        return this.type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
    public Organization getOrganization() {
        return this.organization;
    }
    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
    public String getNo() {
        return this.no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public String getAlias() {
        return this.alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public Date getAddedTime() {
        return this.addedTime;
    }
    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }
    public Date getUpdatedTime() {
        return this.updatedTime;
    }
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    public long getBalanceInFens() {
        return this.balanceInFens;
    }
    public void setBalanceInFens(long balanceInFens) {
        this.balanceInFens = balanceInFens;
    }
}

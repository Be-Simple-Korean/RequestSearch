package com.example.requestsearch.network.data.errata;


import com.example.requestsearch.network.data.BaseVO;

public class ErrAtaVO extends BaseVO {
    String errata;

    public ErrAtaVO(String errata) {
        this.errata = errata;
    }

    public String getErrata() {
        return errata;
    }

    public void setErrata(String errata) {
        this.errata = errata;
    }


}

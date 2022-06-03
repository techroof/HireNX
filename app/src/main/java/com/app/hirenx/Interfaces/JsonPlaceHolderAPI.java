package com.app.hirenx.Interfaces;

import com.app.hirenx.Models.City;
import com.app.hirenx.Models.States;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface JsonPlaceHolderAPI {

    /*@GET("countries")
    @Headers({"X-CSCAPI-KEY: eWE4aXQyQ1BRNU5ua1F2b200c29RbENKbmxISlZjdEdmYnlUVEF3dA=="})
    Call<List<CountryStateCity>> getCountryStateCity();*/


    //states
    @GET("countries/IN/states")
    @Headers({"X-CSCAPI-KEY: eWE4aXQyQ1BRNU5ua1F2b200c29RbENKbmxISlZjdEdmYnlUVEF3dA=="})

    Call<List<States>> getStates();


    //cities

    @GET("countries/{cId}/states/{sIso}/cities")
    @Headers({"X-CSCAPI-KEY: eWE4aXQyQ1BRNU5ua1F2b200c29RbENKbmxISlZjdEdmYnlUVEF3dA=="})

    Call<List<City>> getCities(@Path("cId") int cId,@Path("sIso")  String sIso);

    //getCitiesHireClient
    @GET("countries/IN/cities")
    @Headers({"X-CSCAPI-KEY: eWE4aXQyQ1BRNU5ua1F2b200c29RbENKbmxISlZjdEdmYnlUVEF3dA=="})

    Call<List<City>> getCitiess();



}

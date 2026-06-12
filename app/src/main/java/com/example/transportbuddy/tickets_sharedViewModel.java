package com.example.transportbuddy;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class tickets_sharedViewModel extends ViewModel {
    private final MutableLiveData<String> ticketFrom = new MutableLiveData<>();
    private final MutableLiveData<String> ticketTo = new MutableLiveData<>();
    private final MutableLiveData<String> ticketTime = new MutableLiveData<>();
    private final MutableLiveData<String> ticketDate = new MutableLiveData<>();
    private final MutableLiveData<String> ticketBusNumber = new MutableLiveData<>();
    private final MutableLiveData<String> ticketCount = new MutableLiveData<>();
    private final MutableLiveData<String> ticketCost = new MutableLiveData<>();
    private final MutableLiveData<String> ticketAmount = new MutableLiveData<>();
    private final MutableLiveData<String> totalAmount = new MutableLiveData<>();

    public void setTicketFrom(String placeFrom1) {
        ticketFrom.setValue(placeFrom1);
    }
    public LiveData<String> getTicketFrom() {
        return ticketFrom;
    }

    public void setTicketTo(String placeTo1) {
        ticketTo.setValue(placeTo1);
    }
    public LiveData<String> getTicketTo() {
        return ticketTo;
    }

    public void setTicketTime(String ticketTime1) {
        ticketTime.setValue(ticketTime1);
    }
    public LiveData<String> getTicketTime() {
        return ticketTime;
    }

    public void setTicketDate(String ticketDate1) {
        ticketDate.setValue(ticketDate1);
    }
    public LiveData<String> getTicketDate() {
        return ticketDate;
    }

    public void setTicketBusNumber(String ticketBusNumber1) {
        ticketBusNumber.setValue(ticketBusNumber1);
    }
    public LiveData<String> getTicketBusNumber() {
        return ticketBusNumber;
    }

    public  void setTicketCount(String ticketCount1){
        ticketCount.setValue(ticketCount1);
    }
    public LiveData<String> getTicketCount() {
        return ticketCount;
    }

    public  void setTicketCost(String ticketCost1){
        ticketCost.setValue(ticketCost1);
    }
    public LiveData<String> getTicketCost() {
        return ticketCost;
    }

    public  void setTicketAmount(String ticketAmount1){
        ticketAmount.setValue(ticketAmount1);
    }
    public LiveData<String> getTicketAmount() {
        return ticketAmount;
    }

    public  void setTotalAmount(String totalAmount1){
        totalAmount.setValue(totalAmount1);
    }
    public LiveData<String> getTotalAmount() {
        return totalAmount;
    }
}

package com.collector.enums;

import com.collector.model.CollectorGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Getter
public enum CollectorRoute {

  //SIPLah Activities Collector
  USER_LOGIN("/activities/{sessionId}/{memberRole}/{identifier}/users/{userId}/events/logged_in", 1, CollectorGroup.ACTIVITIES_COLLECTOR),
  USER_LOGOUT("/activities/{sessionId}/{memberRole}/{identifier}/users/{userId}/events/logged_out", 2, CollectorGroup.ACTIVITIES_COLLECTOR),

  //SIPLah Chats Collector
  CHAT_SUBMITTED("/chats/{chatId}/events/submitted",1, CollectorGroup.CHATS_COLLECTOR),

  //SIPLah Comparisons Collector
  COMPARISON_CREATED("/comparisons/{id}/events/created",1, CollectorGroup.COMPARISON_COLLECTOR),

  //SIPLah Items Collector
  PRODUCT_CREATED("/items/{item}/events/created", 1, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_BULK_CREATED("/items/events/bulk_created", 1, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_VERIFIED("/items/{item}/events/verified",2, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_INFO_UPDATED("/items/{item}/events/info_updated",3, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_RATING_UPDATED("/items/{item}/events/rating_updated",3, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_STOCK_UPDATED("/items/{item}/events/stock_updated",3, CollectorGroup.ITEMS_COLLECTOR),
  PRODUCT_PRICE_UPDATED("/items/{item}/events/price_updated",3, CollectorGroup.ITEMS_COLLECTOR),

  //SIPLah Merchants Collector
  MERCHANT_CREATED("/merchants/{merchantId}/events/created",1, CollectorGroup.MERCHANTS_COLLECTOR),
  MERCHANT_VERIFIED("/merchants/{merchantId}/events/verified", 2, CollectorGroup.MERCHANTS_COLLECTOR),
  MERCHANT_UPDATED("/merchants/{merchantId}/events/info_updated",2, CollectorGroup.MERCHANTS_COLLECTOR),
  MERCHANT_DOCUMENT_UPDATED("/merchants/{merchantId}/events/document_updated",2, CollectorGroup.MERCHANTS_COLLECTOR),
  MERCHANT_SHIPPING_UPDATED("/merchants/{merchantId}/events/shipping_updated",2, CollectorGroup.MERCHANTS_COLLECTOR),
  MERCHANT_RATING_UPDATED("/merchants/{merchantId}/events/rating_updated",2, CollectorGroup.MERCHANTS_COLLECTOR),

  //SIPLah Negotiations Collector
  NEGOTIATION_STARTED("/negotiations/{id}/events/started",1, CollectorGroup.NEGOTIATION_COLLECTOR),
  NEGOTIATION_COUNTERED("/negotiations/{id}/events/countered",2, CollectorGroup.NEGOTIATION_COLLECTOR),
  NEGOTIATION_REJECTED("/negotiations/{id}/events/rejected",3, CollectorGroup.NEGOTIATION_COLLECTOR),
  NEGOTIATION_APPROVED("/negotiations/{id}/events/approved",3, CollectorGroup.NEGOTIATION_COLLECTOR),

  //SIPLah Schools Collector
  SCHOOL_CREATED("/schools/{schoolId}/events/created", 1, CollectorGroup.SCHOOLS_COLLECTOR),
  SCHOOL_STATUS_UPDATED("/schools/{schoolId}/events/status_updated", 2, CollectorGroup.SCHOOLS_COLLECTOR),

  //SIPLah Transactions Collector
  ORDER_CREATED("/transactions/{id}/events/created",1, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_INFO_UPDATED("/transactions/{id}/events/info_updated",2, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_CANCELLED("/transactions/{id}/events/order_cancelled",3, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_CLOSED("/transactions/{id}/events/closed",3, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_PROCESSED("/transactions/{transactionId}/events/order_processed",4, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_REJECTED("/transactions/{transactionId}/events/order_rejected",5, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_INFO_UPDATE_PROPOSED("/transactions/{transaction}/events/info_update_proposed", 5, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_INFO_UPDATE_APPROVED("/transactions/{transaction}/events/info_update_approved",6, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_INFO_UPDATE_REJECTED("/transactions/{transaction}/events/info_update_rejected",6, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_CANCELLATION_PROPOSED("/transactions/{transactionId}/events/cancellation_proposed",5, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_CANCELLATION_REJECTED("/transactions/{transactionId}/events/cancellation_rejected",6, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_CANCELLATION_APPROVED("/transactions/{transactionId}/events/cancellation_approved",6, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_SHIPPED("/transactions/{transactionId}/events/order_shipped",7, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_DELIVERY_UPDATED("/transactions/{transactionId}/events/order_delivery_updated", 8, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_DELIVERED("/transactions/{transactionId}/events/order_delivered",9, CollectorGroup.TRANSACTIONS_COLLECTOR),
  COMPLAINT_SUBMITTED("/transactions/{transactionId}/events/complaint_submitted",10, CollectorGroup.TRANSACTIONS_COLLECTOR),
  COMPLAINT_FOLLOWED_UP("/transactions/{transactionId}/events/complaint_followed_up",11, CollectorGroup.TRANSACTIONS_COLLECTOR),
  COMPLAINT_RESOLVED("/transactions/{transactionId}/events/complaint_resolved",12, CollectorGroup.TRANSACTIONS_COLLECTOR),

  AGREEMENT_UPDATED("/transactions/{transactionId}/events/agreement_updated",13, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_RETURNED("/transactions/{transactionId}/events/order_returned",13, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_RECEIVED("/transactions/{transactionId}/events/order_received",14, CollectorGroup.TRANSACTIONS_COLLECTOR),

  ORDER_BAST_GENERATED("/transactions/{id}/events/bast_generated",15, CollectorGroup.TRANSACTIONS_COLLECTOR),
  PAYMENT_CONFIRMED("/transactions/{transactionId}/events/payment_confirmed",15, CollectorGroup.TRANSACTIONS_COLLECTOR),
  PAYMENT_COMPLAINED("/transactions/{transactionId}/events/payment_complained",16, CollectorGroup.TRANSACTIONS_COLLECTOR),
  PAYMENT_SETTLED("/transactions/{transactionId}/events/payment_settled",17, CollectorGroup.TRANSACTIONS_COLLECTOR),
  ORDER_TESTIMONY_SUBMITTED("/transactions/{transaction}/events/testimony_submitted",18, CollectorGroup.TRANSACTIONS_COLLECTOR);

  private final String path;
  private final Integer priority;
  private final Integer group;

  public static void sort(List<CollectorRoute> collectorRoutes){
    Collections.sort(collectorRoutes, Comparator.comparing(CollectorRoute::getGroup).thenComparing(CollectorRoute::getPriority));
  }

}

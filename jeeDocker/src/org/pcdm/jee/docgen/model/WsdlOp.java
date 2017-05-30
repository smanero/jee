package org.pcdm.jee.docgen.model;

public class WsdlOp {
   private String operation;
   private ElType requestType;
   private ElType responseType;

   public WsdlOp(final String _operation, final ElType _requestType, final ElType _responseType) {
      this.operation = _operation;
      this.requestType = _requestType;
      this.responseType = _responseType;
   }

   public String getOp() {
      return operation;
   }

   public ElType getReqType() {
      return requestType;
   }

   public ElType getRspType() {
      return responseType;
   }
}
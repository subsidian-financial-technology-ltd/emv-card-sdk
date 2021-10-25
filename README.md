# EMV-CARD-SDK

#### A NIBSS ISO8583 Message packer and unpakcer with Client for communicating with the acquirer payment processing server.
###### (Supporting SSL/TLS)


ISO8583 (is an international standard for financial transaction card originated interchange messaging) library for Android and also provides a very simple usability as you will see later.

  - Supporting SSL
  - Base on Builder pattern
  - ISO-8583 client SDK for android
  - Working with some enums, it's more readable
  - No heavy dependency

# SDK Usage

## ISO Message
### Create and pack an ISO message
To create an ISO message you must use `ISODataBuilder` which produce `ISOData` which the SDK uses to pack the iso message for you:
> Financial Transaction ISOData(Purchase)

```kotlin
    val messageType = ISOMessageType._0200
    val transactionType = ISOTransactionType.PURCHASE_TRANSACTION_TYPE
    val accountType = ISOAccountType.DEFAULT_ACCTOUNT_TYPE

    ISODataBuilder.Builder()
        .messageType(messageType.value)
        .primaryAccountNumber(transactionUtilities.primaryAccountNumber())
        .processingCode(transactionType.value.plus(accountType.value).plus("00"))
        .transactionAmount(transactionUtilities.transactionAmount())
        .transmissionDateTime(transactionUtilities.transmissionDateTime())
        .settlementConversionRate("")
        .systemTraceAuditNumber(transactionUtilities.systemTraceAuditNumber())
        .localTime(transactionUtilities.localTime())
        .localDate(transactionUtilities.localDate())
        .expirationDate(transactionUtilities.expirationDate())
        .settlementDate("")
        .conversionDate("")
        .merchantType(transactionUtilities.merchantType())
        .posEntryMode(transactionUtilities.posEntryMode())
        .cardSequenceNumber(transactionUtilities.cardSequenceNumber())
        .posConditionCode(transactionUtilities.posConditionCode())
        .posPinCaptureCode(transactionUtilities.posPinCaptureCode())
        .transactionFeeAmount(transactionUtilities.transactionFeeAmount())
        .settlementAmount("")
        .transactionProcessingFeeAmount("")
        .settleProcessingFeeAmount("")
        .acquiringInstitutionId(transactionUtilities.acquiringInstitutionId())
        .forwardingInstitutionId("")
        .trackTwoData(transactionUtilities.trackTwoData())
        .retrievalReferenceNumber(transactionUtilities.retrievalReferenceNumber())
        .authorizationIdResponse("")
        .responseCode("")
        .serviceRestrictionCode(transactionUtilities.serviceRestrictionCode())
        .cardAcceptorTerminalId(transactionUtilities.cardAcceptorTerminalId())
        .cardAcceptorIdCode(transactionUtilities.cardAcceptorIdCode())
        .cardAcceptorNameLocation(transactionUtilities.cardAcceptorNameLocation())
        .additionalResponseData("")
        .additionalData("")
        .transactionCurrencyCode(transactionUtilities.transactionCurrencyCode())
        .settlementCurrencyCode("")
        .pinData("")
        .securityRelatedControlInformation("")
        .additionalAmounts("")
        .integratedCircuitCardData(transactionUtilities.integratedCircuitCardData())
        .messageReasonCode("")
        .authorizingAgentId("")
        .transportEchoData("")
        .paymentInformation("")
        .managementDataOnePrivate("")
        .managementDataTwoPrivate("")
        .primaryMessageHashValue("")
        .extendedPaymentCode("")
        .originalDataElement("")
        .replacementAmount("")
        .payee("")
        .receivingInstitutionId("")
        .accountIdentification1("")
        .accountIdentification2("")
        .posDataCode(transactionUtilities.posDataCode())
        .nearFieldCommunicationData("")
        .secondaryMessageHashValue(transactionUtilities.secondaryMessageHashValue())
        .build()

```

> Network Transaction ISOData(Terminal Master Key Download)
```kotlin
    ISODataBuilder.Builder()
            .processingCode(ISOProcCode.TMK_DOWNLOAD_ISO_PROC_CODE.value)
            .transmissionDateTime(transactionUtilities.transmissionDateTime())
            .systemTraceAuditNumber(transactionUtilities.systemTraceAuditNumber())
            .localTime(transactionUtilities.localTime())
            .localDate(transactionUtilities.localDate())
            .cardAcceptorTerminalId(transactionUtilities.cardAcceptorTerminalId())
            .build()
```

with `ISODataBuilder.Builder()` you can build ISOData object which follows `NIBSS` iso naming format and also packed into iso message. you must call build method to generate the `ISOData` object.

### Unpack a buffer and parse it to ISOData object
For unpacking buffer received from server you need to use `ISOMessageBuilder.unpackMessage()`:
```kotlin
    var serverResponseByteArray = client!!.sendMessageSync(packedTMKISOMessage)

    val isoMessage: ISOData = ISOMessageBuilder.unpackMessage(
            applicationContext, serverResponseByteArray).unpack()
```



## ISOClient
### Creating a simple client
It's very easy to create an server client using `ISOClientBuilder` from the SDK as shown below:
```kotlin
    val client = ISOClientBuilder
                    .createSocket(HOST, PORT)
                    .setEventListener(clientEventListener)
                    .build()

    /** Connect client to the server **/
    client.connect()
```

### Creating a client with SSL/TLS enable and sending message to the server
Sending a message to ISO server over SSL/TLS requires extra methods to be called:
```kotlin
    val client = ISOClientBuilder
                    .createSocket(HOST, PORT)
                    .enableSSL()
                    .setSSLProtocol("SSL") //TLS, SSL, TLSv1.2
                    .setKeyManagers(arrayOf())
                    .setTrustManagers(arrayOf())
                    .setEventListener(clientEventListener)
                    .build()

    /** Connect client to the server **/
    client.connect()
```

it's enough to adding `.enableSSL()` and another requirement parameters before `.build()` and you may need to prepare KeyStore and other things before. TrustManagers can be null.

### Sending message to the server
Sending message to the server and received response from server can be done with `ISOClient.sendMessageSync()` method:
```kotlin
    /** Build your ISOData object **/
    val tmkISOData = TMKRequest.build()

    /** Pass your ISOData to ISOMessageBuilder to pack your data to EMV ISO data format **/
    val packedTMKISOMessage: ISOMessageBuilder.PackBuilder = ISOMessageBuilder
                    .packMessage(applicationContext, "", "")
                    .createTMKDownloadRequest(tmkISOData)

    /** Send message to server **/
    client!!.sendMessageSync(packedTMKISOMessage)

    /** Disconnect client **/
    client.disconnect()
```
Also consider wrapping `client!!.sendMessageSync(packedTMKISOMessage)` with a try catch for the `ISOClientException` and `IOException`

### Client event listener
To get events from the client about connection states and message exchange point, you should pass the `ISOClientEventListener` to the client:
```kotlin
    private val clientEventListener = object : ISOClientEventListener {
        override fun connecting() {
            println("${this.javaClass.simpleName} ==> Client Connecting.")
        }
        override fun connected() {
            println("${this.javaClass.simpleName} ==> Client Connected.")
        }
        override fun connectionFailed() {
            println("${this.javaClass.simpleName} ==> Client Connection Failed.")
        }
        override fun connectionClosed() {
            println("${this.javaClass.simpleName} ==> Client Connection Closed.")
        }
        override fun connectionTimeout() {
            println("${this.javaClass.simpleName} ==> Client Connection Timeout.")
        }
        override fun disconnected() {
            println("${this.javaClass.simpleName} ==> Client Disconnected.")
        }
        override fun beforeSendingMessage() {
            println("${this.javaClass.simpleName} ==> Client Before Sending Message.")
        }
        override fun afterSendingMessage() {
            println("${this.javaClass.simpleName} ==> Client After Sending Message.")
        }
        override fun onReceiveData() {
            println("${this.javaClass.simpleName} ==> Client Message Received.")
        }
        override fun beforeReceiveResponse() {
            println("${this.javaClass.simpleName} ==> Client Before Message Receive.")
        }
        override fun afterReceiveResponse() {
            println("${this.javaClass.simpleName} ==> Client After Message Receive.")
        }
    }
```

### Disconnect the client
```kotlin
    client.disconnect()
```

## ISOData (Reference)
```kotlin
    data class ISOData(
        var messageType: String? = null,
        var primaryAccountNumber: String? = null,           //    2 Primary account number Mandatory
        var processingCode: String? = null,                 //    3 Processing code Mandatory
        var transactionAmount: String? = null,              //    4 Amount, transaction Mandatory
        var transmissionDateTime: String? = null,           //    7 Transmission date and time Conditional
        var settlementConversionRate: String? = null,       //    9 Conversion rate, settlement Conditional
        var systemTraceAuditNumber: String? = null,         //    11 Systems trace audit number Conditional
        var localTime: String? = null,                      //    12 Time, local transaction Mandatory
        var localDate: String? = null,                      //    13 Date, local transaction Conditional
        var expirationDate: String? = null,                 //    14 Date, expiration Conditional
        var settlementDate: String? = null,                 //    15 Date, Settlement Conditional
        var conversionDate: String? = null,                 //    16 Date, conversion Conditional
        var merchantType: String? = null,                   //    18 Merchant’s type Mandatory
        var posEntryMode: String? = null,                   //    22 POS entry mode Mandatory
        var cardSequenceNumber: String? = null,             //    23 Card sequence number Conditional
        var posConditionCode: String? = null,               //    25 POS condition code Mandatory
        var posPinCaptureCode: String? = null,              //    26 POS PIN capture code Conditional
        var transactionFeeAmount: String? = null,           //    28 Amount, transaction fee Conditional
        var settlementAmount: String? = null,               //    29 Amount, settlement fee Conditional
        var transactionProcessingFeeAmount: String? = null, //    30 Amount, transaction processing fee Conditional
        var settleProcessingFeeAmount: String? = null,      //    31 Amount, settle processing fee Conditional
        var acquiringInstitutionId: String? = null,         //    32 Acquiring institution id code Mandatory
        var forwardingInstitutionId: String? = null,        //    33 Forwarding institution id code Conditional
        var trackTwoData: String? = null,                   //    35 Track 2 data Conditional
        var retrievalReferenceNumber: String? = null,       //    37 Retrieval reference number Mandatory
        var authorizationIdResponse: String? = null,        //    38 Authorization id response Conditional
        var responseCode: String? = null,                   //    39 Response code Mandatory
        var serviceRestrictionCode: String? = null,         //    40 Service restriction code Conditional
        var cardAcceptorTerminalId: String? = null,         //    41 Card acceptor terminal id Optional
        var cardAcceptorIdCode: String? = null,             //    42 Card acceptor id code Conditional
        var cardAcceptorNameLocation: String? = null,       //    43 Card acceptor name/location Conditional
        var additionalResponseData: String? = null,         //    44 Additional response data Optional
        var additionalData: String? = null,                 //    48 Additional data Conditional
        var transactionCurrencyCode: String? = null,        //    49 Currency code, transaction Mandatory
        var settlementCurrencyCode: String? = null,         //    50 Currency code, settlement Conditional
        var pinData: String? = null,                        //    52 PIN data Conditional
        var securityRelatedControlInformation: String? = null,   //    53 Security related control information Conditional
        var additionalAmounts: String? = null,              //    54 Additional amounts Conditional
        var integratedCircuitCardData: String? = null,      //    55 Integrated Circuit Card System Related Data Conditional
        var messageReasonCode: String? = null,              //    56 Message reason code Optional
        var authorizingAgentId: String? = null,             //    58 Authorizing agent id code Conditional
        var transportEchoData: String? = null,              //    59 Transport (echo) data Conditional
        var paymentInformation: String? = null,             //    60 Payment Information Conditional
        var managementDataOnePrivate: String? = null,       //    62 Private, management data 1 Conditional
        var managementDataTwoPrivate: String? = null,       //    63 Private, management data 2 Conditional
        var primaryMessageHashValue: String? = null,        //    64 Primary Message Hash Value Conditional
        var extendedPaymentCode: String? = null,            //    67 Extended payment code Conditional
        var originalDataElement: String? = null,            //    90 Original data elements Mandatory
        var replacementAmount: String? = null,              //    95 Replacement Amounts Mandatory
        var payee: String? = null,                          //    98 Payee Conditional
        var receivingInstitutionId: String? = null,         //    100 Receiving Institution ID Code Optional
        var accountIdentification1: String? = null,         //    102 Account identification 1 Optional
        var accountIdentification2: String? = null,         //    103 Account identification 2 Optional
        var posDataCode: String? = null,                    //    123 POS data code Mandatory
        var nearFieldCommunicationData: String? = null,     //    124 Near Field Communication Data Conditional
        var secondaryMessageHashValue: String? = null,      //    128 Secondary Message Hash Value Mandatory
    )
```

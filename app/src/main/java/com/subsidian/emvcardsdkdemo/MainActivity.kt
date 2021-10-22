package com.subsidian.emvcardsdkdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.subsidian.emvcardmanager.builders.ISOClientBuilder
import com.subsidian.emvcardmanager.builders.ISOMessageBuilder
import com.subsidian.emvcardmanager.interfaces.ISOClient
import com.subsidian.emvcardmanager.interfaces.ISOClientEventListener
import com.subsidian.emvcardsdkdemo.utils.transaction.TMKRequest


class MainActivity : AppCompatActivity() {

    var client: ISOClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            try {
                client = ISOClientBuilder
                    .createSocket("arca-pos.qa.arca-payments.network", 11000)
                    .configureBlocking(false)
                    .enableSSL()
                    .setSSLProtocol("SSL") //TLS, SSL, TLSv1.2
                    .setKeyManagers(arrayOf())
                    .setTrustManagers(arrayOf())
                    .setEventListener(clientEventListener)
                    .build()
                /** Connect the client socket to the server**/
                client!!.connect()
                /** Send and Read data response **/
//                val response: String = Arrays.toString(
//                    client.sendMessageSync(
//                        ISOMessageBuilder.createMessage(applicationContext)
//                            .createTMKDownloadRequest()
//                    )
//                )
//                ISOMessageBuilder.unpackMessage(applicationContext, client.sendMessageSync(
//                    ISOMessageBuilder.createMessage(applicationContext)
//                        .createTMKDownloadRequest()
//                )).build()
//                println("response = $response")
                /** Disconnect from the client socket **/
//                client.disconnect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
    }


    private val clientEventListener = object : ISOClientEventListener {
        override fun connecting() {
            println("${this.javaClass.simpleName} ==> Client Connecting.")
        }
        override fun connected() {
            println("${this.javaClass.simpleName} ==> Client Connected.")
            if (client != null) {
                ISOMessageBuilder.unpackMessage(
                    applicationContext, client!!.sendMessageSync(
                        ISOMessageBuilder.createMessage(applicationContext)
                            .createTMKDownloadRequest(TMKRequest.build())
                    )
                ).build()
                /** Disconnect from the client socket **/
                client!!.disconnect()
            }
        }
        override fun connectionFailed() {
            println("${this.javaClass.simpleName} ==> Client Connection Failed.")
        }
        override fun connectionClosed() {
            println("${this.javaClass.simpleName} ==> Client Connection Closed.")
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

}
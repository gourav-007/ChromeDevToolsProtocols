package org.tests;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.emulation.Emulation;
import org.openqa.selenium.devtools.v85.log.Log;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.ConnectionType;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestChromeDevTools {

    ChromeDriver driver;
    DevTools devTools;
    @BeforeTest
    public void setup(){
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        devTools = driver.getDevTools();
    }


   // @Test
    public void viewBrowserConsoleLogs(){
        //Get Devtools and Create Session
        devTools.createSession();

        //Enable Console Log
        devTools.send(Log.enable());

        //Add Listener
        devTools.addListener(Log.entryAdded(),logEntry -> {
            System.out.println(".............");
            System.out.println("Text: "+logEntry.getText());
            System.out.println("URL : "+logEntry.getUrl());
            System.out.println("Level: "+logEntry.getLevel());
        } );

        driver.get("http://the-internet.herokuapp.com/broken_images");
    }

   // @Test
    public void mockGeoLocation_Devtools(){
        devTools.createSession();
        devTools.send(Emulation.setGeolocationOverride(Optional.of(52.5043),
                Optional.of(13.4501),
                Optional.of(1)));

        driver.get("https://my-location.org/");
    }

   //@Test
    public void mockGeoLocation_executeCDPCommand(){
        Map cordinates = new HashMap()
        {{
           put("latitute",32.746940);
           put("longitute",-97.092400);
           put("accuracy",1);
        }};

        driver.executeCdpCommand("Emulation.setGeolocationOverride",cordinates);
        driver.get("https://where-am-i.org/");
    }

    //@Test
    public void emulateNetworkEmulation(){
        devTools.createSession();
        devTools.send(Network.enable(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()));

        devTools.send(Network.emulateNetworkConditions(
                false,
                150,
                2500,
                2500,
                Optional.of(ConnectionType.NONE)
        ));

        driver.get("https://linkedin.com");
        System.out.println("Enable Slow N/W : "+driver.getTitle());
    }

    @Test
    public void doNotEnableNWEmulation(){
        driver.get("https://linkedin.com");
        System.out.println("Do not enable N/W : "+driver.getTitle());
    }
}

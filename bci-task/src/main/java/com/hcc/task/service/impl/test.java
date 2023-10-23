package com.hcc.task.service.impl;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import static java.util.concurrent.TimeUnit.SECONDS;

public class test {

    public static void main(String[] args) throws IOException, InterruptedException {
        int a = 2;
        System.out.println(~a);
        String s = "";
        for(int i=0;i<3;i++){
            s = s+"a"+',';
        }
        System.out.println(s);
        s = s.substring(0,s.length()-1);
        System.out.println(s);
        String ip = "10.112.236.95";
        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://"+ip+":2375").build();

        HostConfig hostConfig = new HostConfig().withExtraHosts(new String[]{"server:82.15.29.231"});

//        CreateContainerResponse container = dockerClient.createContainerCmd("python3.10-bci:2.0").withEnv("SYSALGID=01").withHostConfig(hostConfig).withCmd("/bin/sh" , "-c", "python AlgorithmSystemMain.py").exec();
//        CreateContainerResponse container = dockerClient.createContainerCmd("python:3.8").withCmd("/bin/sh" , "-c", "cd /opt&&ls&&pip install -r requirement.txt -i http://pypi.douban.com/simple --trusted-host pypi.douban.com&&python main.py").exec();
        CreateContainerResponse container = dockerClient.createContainerCmd("bci:test")
//                .withEnv("SYSALGID=01")
//                .withHostConfig(hostConfig)
                .withCmd("/bin/sh" , "-c", "python main.py").exec();
//        FileInputStream fis = new FileInputStream("C:\\Users\\13016\\Desktop\\hhh.tar.gz");
//        GZIPInputStream gis = new GZIPInputStream(fis);
//////        TarArchiveInputStream taris = new TarArchiveInputStream(gis);
//        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis).withRemotePath("/opt").exec();
//        FileInputStream fis2 = new FileInputStream("C:\\Users\\13016\\Desktop\\AlgorithmSystem\\ssvep.tar.gz");
//        GZIPInputStream gis2 = new GZIPInputStream(fis2);
//        dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(gis2).withRemotePath("/opt").exec();
        dockerClient.startContainerCmd(container.getId()).exec();
//        dockerClient.createContainerCmd("python").withEnv("SYSALGID=01");
//        List<Container> containers = dockerClient.listContainersCmd().exec();
        System.out.println(container.getId());
//        Thread.sleep(30000);
        Integer integer = -1;
        try{
            integer = dockerClient.waitContainerCmd(container.getId()).exec(new WaitContainerResultCallback()).awaitStatusCode(1000, SECONDS);
        }catch (Exception e){
            integer = 2;
        }

        System.out.println(integer);
        if(integer == 2){
            System.out.println("timeout");
        }else{
            dockerClient.logContainerCmd(container.getId()).withStdOut(true).withStdErr(true).exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame item) {
//                super.onNext(item);
                    System.out.println(item.toString());
//                System.out.println(item.getPayload());
//                System.out.println(item.getStreamType().toString());
                }
            }).awaitCompletion();
        }
//        dockerClient.removeContainerCmd(container.getId()).exec();
        dockerClient.close();



//        String tarPath = "C:\\Users\\13016\\Desktop\\test.tar.gz";
//        String s = readTarGz(tarPath);
//        String[] strings = s.split(":");
//        System.out.println(s);
//        System.out.println(strings[1]);
//        System.out.println(strings[3]);
    }

    public static String readTarGz(String tarPath) throws IOException {
        File targzFile = new File(tarPath);
        FileInputStream fileIn = null;
        BufferedInputStream bufIn = null;
        GZIPInputStream gzipIn = null;
        TarArchiveInputStream taris = null;
        try {
            fileIn = new FileInputStream(targzFile);
//            bufIn = new BufferedInputStream(fileIn);
            gzipIn = new GZIPInputStream(fileIn);
            taris = new TarArchiveInputStream(gzipIn);
            TarArchiveEntry entry = null;
            while ((entry = taris.getNextTarEntry()) != null) {
                if (entry.isDirectory())
                    continue;
//                System.out.println(entry.getName());
                if(entry.getName().equals("config.json")){
                    byte[] b = new byte[(int) entry.getSize()];
                    taris.read(b, 0, (int) entry.getSize());
                    return new String(b);
                }
            }
        } finally {
            taris.close();
            gzipIn.close();
//            bufIn.close();
            fileIn.close();
        }
        return null;
    }


}

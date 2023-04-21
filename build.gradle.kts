val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "com.mizikarocco"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {

    sshAntTask("org.apache.ant:ant-jsch:1.10.12")

    implementation("commons-codec:commons-codec:1.15")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-server-freemarker:$ktor_version")

    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    //implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")

    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

ant.withGroovyBuilder {
    "taskdef"(
        "name" to "scp",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
        "classpath" to configurations.get("sshAntTask").asPath
    )
    "taskdef"(
        "name" to "ssh",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
        "classpath" to configurations.get("sshAntTask").asPath
    )
}
//
//task.register<Tar>("copyDirectory") {
//    // Local directory to copy
//
//    // Remote directory to copy to
//    def remoteDir = '/path/to/remote/directory'
//
//    // SSH connection details
//    def sshUser = 'username'
//    def sshHost = 'example.com'
//    def sshKey = file('path/to/ssh/key')
//
//    val localDir = file("src/main/resources/static")
//    // Create a temporary tar archive of the local directory
//    val tarFile = file("${localDir}.tar")
//    doLast {
//        ant.tar(destfile: tarFile, basedir: localDir)
//    }
//    val key = file("keys/mizikaroccoBackend")
//
//    exec{
//        commandLine("scp", "-i", key, tarFile, "${sshUser}@${sshHost}:${remoteDir}")
//
//    }
//    // Copy the tar archive to the remote server
//    commandLine 'ssh', '-i', sshKey, "${sshUser}@${sshHost}", 'mkdir', '-p', remoteDir
//    commandLine 'ssh', '-i', sshKey, "${sshUser}@${sshHost}", 'tar', '-C', remoteDir, '-xf', "${remoteDir}/${localDir}.tar"
//}

task("deploy") {
    dependsOn("clean", "shadowJar")
    ant.withGroovyBuilder {
        doLast {
            val knownHosts = File.createTempFile("knownhosts", "txt")
            val user = "root"
            val host = "193.43.134.143"
            val key = file("keys/mizikaroccoBackend")
            val jarFileName = "com.mizikarocco.karaokeappbackend-all.jar"
            try {
                "scp"(
                    "file" to file("build/libs/$jarFileName"),
                    "todir" to "$user@$host:/root/KaraokeAppBackend",
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts
                )
//                "scp"(
//                    "file" to file("src/main/resources/static"),
//                    "todir" to "$user@$host:/root/KaraokeAppBackend",
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "recursive" to true,
//                    "overwrite" to true
//
//                )
//                "scp"(
//                    "file" to file("src/main/resources/database"),
//                    "todir" to "$user@$host:/root/KaraokeAppBackend",
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "recursive" to true,
//                    "overwrite" to true
//                )
//                "scp"(
//                    "file" to file("src/main/resources/templates"),
//                    "todir" to "$user@$host:/root/KaraokeAppBackend",
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "recursive" to true,
//                    "overwrite" to true
//                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "mv /root/KaraokeAppBackend/$jarFileName /root/KaraokeAppBackend/KaraokeAppBackend.jar"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl stop KaraokeAppBackend"
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to key,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "systemctl start KaraokeAppBackend"
                )
            } finally {
                knownHosts.delete()
            }
        }
    }
}
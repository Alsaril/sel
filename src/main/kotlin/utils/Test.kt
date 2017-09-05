package utils

fun main(args: Array<String>) {
    val settings = Settings.create("cfg.txt") {
        "token" to "23qw54"
        "username" to "user1"
        "inn" to "234234234234"
    }

    println(settings["token"].stringValue())
    settings["token"] = "ahrxg"
    println(settings["token"].stringValue())
    settings.forceSave()
}
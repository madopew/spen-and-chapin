fun sin() : Double {
    val eps = 1e-4
    var y : Double
    var x : Double
    var n : Integer
    var vs : Double

    var t = x - y
    t /= 3
    t = readLine()!!
    t += 8
    println(t)

    x = abs(valueOf(readLine()!!))
    y = x
    n = 2
    vs = x
    do {
        vs = - vs * x * x / (2 * n - 1) / (2 * n - 2) //ok
        n = n + 1
        y += vs
    } while (abs(vs) >= eps)
    println(x, y, eps)
}
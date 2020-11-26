fun main(a : Double) {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int = 3
    val PI : Double = 3.14
    PI += 0.001592
    x = a
    y = a
    det = toDouble(readLine()!!) + a
    do {
        det = -det * x * x / ((n + 1)*n)
        n++
        y += det
    } while (getAbs(det) > eps)
    println(y)
}
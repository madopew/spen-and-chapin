fun findExp(a : Double) : Double {
    val eps : Double = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int = 1
    x = a
    y = 1.0
    det = 1.0
    do {
        det = det * x / n
        n++
        y += det
    } while (getAbs(det) > eps)
    return y
}
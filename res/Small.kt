fun findExp() : Double {
    val a = Integer.valueOf(readLine()!!)
    val eps = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int
    x = a
    println(getAbs(x++))
    y = n = 1
    var t = x - y
    det = 1.0
    do {
        det = det * x / n++
        y += det;
    } while (getAbs(det) > eps)
    return y
}

// a, eps, x, y, det, n, t
// T - t
// C - det, eps
// M - y, n
// P - a, x
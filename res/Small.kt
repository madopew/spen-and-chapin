fun findExp() : Double {
    val a = 3.53
    val eps = 0.00001
    var x : Double
    var y : Double
    var det : Double
    var n : Int = 1
    x = a
    y = 1.0
    var t = x - y
    t += 5
    det = 1.0
    do {
        det = det * x / n
        n++
        y += det;
    } while (getAbs(det) > eps)
    return y
}

// a, eps, x, y, det, n, t
// T - t
// C - det, eps
// M - y, n
// P - a, x
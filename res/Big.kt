fun main() {
    print(factorial(4))

    val list = {3, 2, 9, 7, 1, 10, -2};
    var swap = true
    while(swap){
        swap = false
        for(i in 0 until size(list)-1){
            if(list[i] > list[i+1]){
                val temp = list[i]
                list[i] = list[i+1]
                list[i + 1] = temp

                swap = true
            }
        }
    }

    var value = 1275
    var amount = 0
    while (value != 0) {
        value /= 10
        amount++
    }
    println(amount)

    val a = toInt(readLine()!!)
    if (a < 0)
        println("negative")
    else
        println("positive")

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

    for(i in 1..10)
        for(j in 1..10)
            for(k in 1..10)
                for(l in 1..10)
                    for(m in 1..10)
                        for(n in 1..10) {
                            val num = i + j + k + l + m + n
                            if(num < 50) {
                                when(num) {
                                    10 -> println("yes it's 10")
                                    20 -> println("i'm almost 20 too!")
                                    else -> println("most common answer!")
                                }
                            } else if(num == 50)
                                println("unlikely to happen")
                            else if(num == 60)
                                println("also unlikely")
                        }
    println("how much time did it take?????")

    var sum;
    var bigger;
    for(i in 1..100000) {
        sum++;
        bigger = sum + ++bigger
    }
    println(sum + bigger)
}
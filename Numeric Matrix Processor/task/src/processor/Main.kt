package processor

import java.util.*

val scanner = Scanner(System.`in`)

fun main() {


    while (true) {

        println("1. Add matrices\n" +
                "2. Multiply matrix by a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit")


        print("Your choice: ")
        when (val op = readLine()!!.toInt()) {

            0 -> { break }
            1 -> { calcMatrices(op) }
            2 -> { multiByConst() }
            3 -> { calcMatrices(op) }
            4 -> {

                println("1. Main diagonal\n" +
                        "2. Side diagonal\n" +
                        "3. Vertical line\n" +
                        "4. Horizontal line")

                when (val transportOp = readLine()!!.toInt()) {

                    1 -> { transportMatrix(transportOp) }
                    2 -> { transportMatrix(transportOp) }
                    3 -> { transportMatrix(transportOp) }
                    4 -> { transportMatrix(transportOp) }
                }
            }
            5 -> {

                print("Enter size of matrix: ")
                val (rows, cols) = Array(2) { scanner.nextInt() }

                val matrix = readMatrix(rows, cols)
                println("The result is:")
                println(calcDeterminant(matrix, rows))
            }
            6 -> {

                print("Enter size of matrix: ")
                val (rows, cols) = Array(2) { scanner.nextInt() }
                val matrix = readMatrix(rows, cols)

                val inverse = Array(rows){DoubleArray(cols)}
                inverse(matrix, inverse, rows)
                println("The result is:")
                printMatrix(inverse, rows, rows)
            }
        }
    }
}

fun readMatrix(rows: Int, cols: Int): Array<DoubleArray> {

    val matrix = Array(rows){DoubleArray(cols)}

    for (x in 0 until rows) for (y in 0 until cols) matrix[x][y] = scanner.next().toDouble()

    return matrix
}

fun getCofactor(mat: Array<DoubleArray>, p: Int, q: Int, n: Int): Array<DoubleArray> {
    var i = 0
    var j = 0

    val temp =  Array(n){ DoubleArray(n) }
    for (row in 0 until n) {
        for (col in 0 until n) {

            if (row != p && col != q) {
                temp[i][j++] = mat[row][col]

                if (j == n - 1) {
                    j = 0
                    i++
                }
            }
        }
    }

    return temp
}

fun calcDeterminant(mat: Array<DoubleArray>, rows: Int): Double {

    var d = 0.0
    if (rows == 1) return mat[0][0]

    var temp: Array<DoubleArray>

    var sign = 1

    for (f in 0 until rows) {
        temp = getCofactor(mat, 0, f, rows)
        d += (sign * mat[0][f]
                * calcDeterminant(temp, rows - 1))

        sign = -sign
    }
    return d
}

// Function to get adjoint of A[N][N] in adj[N][N].
fun adjoint(A: Array<DoubleArray>, adj: Array<DoubleArray>, rows: Int) {

    if (rows == 1) {

        adj[0][0] = 1.0
        return
    }

    // temp is used to store cofactors of A[][]
    var sign: Int
    var temp: Array<DoubleArray>
    for (i in 0 until rows) {
        for (j in 0 until rows) {
            // Get cofactor of A[i][j]
           temp = getCofactor(A, i, j, rows)

            // sign of adj[j][i] positive if sum of row
            // and column indexes is even.
            sign = if ((i + j) % 2 == 0) 1 else - 1

            // Interchanging rows and columns to get the
            // transpose of the cofactor matrix
            adj[j][i] = sign * calcDeterminant(temp, rows - 1)
        }
    }
}

// Function to calculate and store inverse, returns false if
// matrix is singular
fun inverse(A: Array<DoubleArray>, inverse: Array<DoubleArray>, rows: Int): Boolean {
    // Find determinant of A[][]
    val det = calcDeterminant(A, rows)
    if (det == 0.0) {
        print("Singular matrix, can't find its inverse")
        return false
    }

    // Find adjoint
    val adj = Array(rows) { DoubleArray(rows) }
    adjoint(A, adj, rows)

    // Find Inverse using formula "inverse(A) = adj(A)/det(A)"
    for (i in 0 until rows) for (j in 0 until rows) inverse[i][j] = adj[i][j] / det
    return true
}

fun calcMatrices(op: Int) {

    print("Enter size of first matrix: ")
    val (rowA, colA) = Array(2) { scanner.nextInt() }
    println("Enter first matrix:")
    val matrixA = Array(rowA){DoubleArray(colA)}

    for (x in 0 until rowA) for (y in 0 until colA) matrixA[x][y] = scanner.next().toDouble()


    print("Enter size of second matrix: ")
    val (rowB, colB) = Array(2) { scanner.nextInt() }

    if (op == 1 && (rowA != rowB || colA != colB)) {
        println("The operation cannot be performed.")
        return
    }
    if (op == 3 && colA != rowB) {
        println("The operation cannot be performed.")
        return
    }

    println("Enter second matrix:")
    val matrixB = Array(rowB){DoubleArray(colB)}

    for (x in 0 until rowB) for (y in 0 until colB) matrixB[x][y] = scanner.next().toDouble()

    if (op == 1) {

        addMatrix(matrixA, matrixB, rowA, colA)
    } else {

        multiMatrices(matrixA, matrixB, rowA, colA, colB)
    }
}

fun printMatrix(matrix: Array<DoubleArray>, rows: Int, cols: Int) {

    for (x in 0 until rows) {
        for (y in 0 until cols) {

            val n = matrix[x][y]
            print(if (n.rem(1).equals(0.0)) "${n.toInt()} " else "$n ")
        }
        println()
    }
    println()
}

fun addMatrix(matrixA: Array<DoubleArray>, matrixB: Array<DoubleArray>, rows: Int, cols: Int) {

    val addMatrix = Array(rows) { DoubleArray(cols) }
    for (x in 0 until rows) for (y in 0 until cols) addMatrix[x][y] = matrixA[x][y] + matrixB[x][y]

    println("The result is:")
    printMatrix(addMatrix, rows, cols)
}

fun multiByConst() {

    val scanner = Scanner(System.`in`)
    print("Enter size of matrix: ")
    val (rows, cols) = Array(2) { scanner.nextInt() }
    val matrix = readMatrix(rows, cols)

    print("Enter constant: ")
    val constant = scanner.next().toDouble()

    for (x in 0 until rows) for (y in 0 until cols) matrix[x][y] = matrix[x][y] * constant

    println("The result is:")
    printMatrix(matrix, rows, cols)

}

fun multiMatrices(matrixA: Array<DoubleArray>, matrixB: Array<DoubleArray>, rows: Int, colA: Int, cols: Int) {

    val multiMatrix = Array(rows){DoubleArray(cols)}
    for (x in 0 until rows) {
        for (y in 0 until cols) {
            for (e in 0 until colA) {

                multiMatrix[x][y] += matrixA[x][e] * matrixB[e][y]
            }
        }
    }

    println("The result is:")
    printMatrix(multiMatrix, rows, cols)
}

fun transportMatrix(op: Int) {

    print("Enter size of matrix: ")
    val (rows, cols) = Array(2) { scanner.nextInt() }
    val matrix = Array(rows){DoubleArray(cols)}

    val main = Array(rows){DoubleArray(cols)}
    val side = Array(rows){DoubleArray(cols)}
    val vertical = Array(rows){DoubleArray(cols)}
    val horizontal = Array(rows){DoubleArray(cols)}

    for (x in 0 until rows) for (y in 0 until cols) matrix[x][y] = scanner.next().toDouble()


    for (x in 0 until rows) {
        for (y in 0 until cols) {

            main[y][x] = matrix[x][y]
            side[x][y] = matrix[cols - 1 - y][rows - 1 - x]
            vertical[x][y] = matrix[x][cols - 1 - y]
            horizontal[x][y] = matrix[rows - 1 - x][y]
        }
    }

    println("The result is:")
    when (op) {

        1 -> { printMatrix(main, rows, cols)}
        2 -> { printMatrix(side, rows, cols)}
        3 -> { printMatrix(vertical, rows, cols)}
        4 -> { printMatrix(horizontal, rows, cols)}
    }
}
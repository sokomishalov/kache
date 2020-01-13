package ru.sokomishalov.kache.core.util

/**
 * @author sokomishalov
 */

// https://stackoverflow.com/a/1248627/5843129
fun String.globToRegex(): Regex {
    val sb = StringBuilder(length)
    var inGroup = 0
    var inClass = 0
    var firstIndexInClass = -1
    val arr = toCharArray()
    var i = 0
    while (i < arr.size) {
        when (val ch = arr[i]) {
            '\\' -> if (++i >= arr.size) {
                sb.append('\\')
            } else {
                val next = arr[i]
                when (next) {
                    ',' -> {
                    }
                    'Q', 'E' -> {
                        // extra escape needed
                        sb.append('\\')
                        sb.append('\\')
                    }
                    else -> sb.append('\\')
                }// escape not needed
                sb.append(next)
            }
            '*' -> if (inClass == 0)
                sb.append(".*")
            else
                sb.append('*')
            '?' -> if (inClass == 0)
                sb.append('.')
            else
                sb.append('?')
            '[' -> {
                inClass++
                firstIndexInClass = i + 1
                sb.append('[')
            }
            ']' -> {
                inClass--
                sb.append(']')
            }
            '.', '(', ')', '+', '|', '^', '$', '@', '%' -> {
                if (inClass == 0 || firstIndexInClass == i && ch == '^')
                    sb.append('\\')
                sb.append(ch)
            }
            '!' -> if (firstIndexInClass == i)
                sb.append('^')
            else
                sb.append('!')
            '{' -> {
                inGroup++
                sb.append('(')
            }
            '}' -> {
                inGroup--
                sb.append(')')
            }
            ',' -> if (inGroup > 0)
                sb.append('|')
            else
                sb.append(',')
            else -> sb.append(ch)
        }
        i++
    }
    return sb.toString().toRegex()
}
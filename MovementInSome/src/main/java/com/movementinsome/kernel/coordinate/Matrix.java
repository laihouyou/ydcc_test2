package com.movementinsome.kernel.coordinate;

/**
 * 矩阵运算类
 * 
 * @author gordon
 * 
 */
public class Matrix {

	// / <summary>
	// / 矩阵的列数
	// / </summary>
	public int columnCount;

	// / <summary>
	// / 矩阵的行数
	// / </summary>
	public int rowCount;

	// / <summary>
	// / 矩阵数据
	// / </summary>
	public double[][] data;

	// / <summary>
	// / 输出矩阵时每个元素所占宽度
	// / </summary>
	public int strWidth;

	// / <summary>
	// / 默认构造函数
	// / </summary>
	public Matrix() {
		rowCount = 1;
		columnCount = 1;
		data = new double[rowCount][columnCount];
	}

	// / <summary>
	// / 指定行列数构造函数
	// / </summary>
	// / <param name="rowCount">矩阵行数</param>
	// / <param name="columnCount">矩阵列数</param>
	public Matrix(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = new double[rowCount][columnCount];
	}

	// / <summary>
	// / 方阵构造函数
	// / </summary>
	// / <param name="nSize">方阵行数</param>
	public Matrix(int nSize) {
		this.rowCount = nSize;
		this.columnCount = nSize;
		this.data = new double[rowCount][columnCount];
	}

	public Matrix(double[][] value) {
		this.rowCount = value[1].length;
		this.columnCount = value[0].length;
		this.data = new double[rowCount][columnCount];
		// 赋值
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				data[i][j] = value[i][j];
			}
		}
	}

	// / <summary>
	// / 指定值构造函数
	// / </summary>
	// / <param name="rowCount">数组行数</param>
	// / <param name="columnCount">数组列数</param>
	// / <param name="value">数组值</param>
	public Matrix(int rowCount, int columnCount, double[][] value) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.data = new double[rowCount][columnCount];
		// 赋值
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				this.data[i][j] = value[i][j];
			}
		}
	}

	// / <summary>
	// / 获取指定行列元素的值
	// / </summary>
	// / <param name="rowNum">指定行</param>
	// / <param name="colNum">指定列</param>
	// / <returns>返回数值</returns>
	public double getElement(int rowNum, int colNum) {
		return data[rowNum][colNum];
	}

	// / <summary>
	// / 获取指定行元素的值
	// / </summary>
	// / <param name="rowNum">行号</param>
	// / <returns></returns>
	public double[] getRow(int rowNum) {
		double[] value = new double[columnCount];
		for (int i = 0; i < columnCount; i++) {
			value[i] = data[rowNum][i];
		}
		return value;
	}

	// / <summary>
	// / 获取指定列元素的值
	// / </summary>
	// / <param name="colNum">列号</param>
	// / <returns></returns>
	public double[] getColumn(int colNum) {
		double[] value = new double[rowCount];
		for (int i = 0; i < rowCount; i++) {
			value[i] = data[i][colNum];
		}
		return value;
	}

	// / <summary>
	// / 设置指定行列元素的值
	// / </summary>
	// / <param name="row">指定行</param>
	// / <param name="column">指定列</param>
	// / <param name="value">指定值</param>
	// / <returns>是否赋值成功</returns>
	public boolean setElement(int row, int column, double value) {
		if (row * column < 0)// 行列数不能为负值
			return false;
		else if (row > rowCount || column > columnCount)// 指定行列数必须存在
			return false;
		else {
			data[row][column] = value;
			return true;
		}
	}

	// / <summary>
	// / 设置指定行元素的值
	// / </summary>
	// / <param name="rowNum">行号</param>
	// / <param name="value">数组</param>
	// / <returns>是否赋值成功</returns>
	public boolean setRow(int rowNum, double[] value) {
		if (rowNum < 0 || rowNum > rowCount)// 行编号必须存在
			return false;
		else if (value.length < columnCount)
			return false;
		else {
			for (int i = 0; i < columnCount; i++)
				setElement(rowNum, i, value[i]);
			return true;
		}
	}

	// / <summary>
	// / 设置指定列元素的值
	// / </summary>
	// / <param name="columnNum">列号</param>
	// / <param name="value">数组</param>
	// / <returns>是否赋值成功</returns>
	public boolean setColumn(int columnNum, double[] value) {
		if (columnNum < 0 || columnNum > columnCount)// 行编号必须存在
			return false;
		else if (value.length < rowCount)
			return false;
		else {
			for (int i = 0; i < rowCount; i++)
				setElement(i, columnNum, value[i]);
			return true;
		}
	}

	/*
	 * /// <summary> /// 设置矩阵所有元素值 /// </summary> /// <param
	 * name="value"></param> public void setData(double[][] value) {
	 * this.rowCount = value[1].length; columnCount = value[0].length; this.data
	 * = new double[rowCount][columnCount]; // 赋值 for (int i = 0; i < rowCount;
	 * i++) { for (int j = 0; j < columnCount; j++) { data[i][j] = value[i][j];
	 * } } }
	 */

	/*
	 * /// <summary> /// 获取矩阵 /// </summary> /// <returns></returns> public
	 * double[][] getData() { return data; }
	 */

	// 重载加法运算符,+操作运算
	public static Matrix _add(Matrix leftM, Matrix rightM) {
		if (leftM.rowCount != rightM.rowCount
				|| leftM.columnCount != rightM.columnCount)
			return null;
		else {
			Matrix matrix = new Matrix(leftM.rowCount, leftM.columnCount);
			for (int i = 0; i < matrix.rowCount; i++) {
				for (int j = 0; j < matrix.columnCount; j++) {
					matrix.data[i][j] = leftM.data[i][j] + rightM.data[i][j];
				}
			}

			return matrix;
		}
	}

	// 重载减法运算符,-运算符
	public static Matrix _dec(Matrix leftM, Matrix rightM) {
		if (leftM.rowCount != rightM.rowCount
				|| leftM.columnCount != rightM.columnCount)
			return null;
		else {
			Matrix matrix = new Matrix(leftM.rowCount, leftM.columnCount);
			for (int i = 0; i < matrix.rowCount; i++) {
				for (int j = 0; j < matrix.columnCount; j++) {
					matrix.data[i][j] = leftM.data[i][j] - rightM.data[i][j];
				}
			}

			return matrix;
		}
	}

	// 重载乘法运算符1（矩阵与数值相乘）
	public static Matrix _mul(Matrix matrix, double value) {
		for (int i = 0; i < matrix.rowCount; i++) {
			for (int j = 0; j < matrix.columnCount; j++) {
				matrix.data[i][j] *= value;
			}
		}
		return matrix;
	}

	// 重载乘法运算符2（数值与矩阵相乘）
	public static Matrix _mul(double value, Matrix matrix) {
		for (int i = 0; i < matrix.rowCount; i++) {
			for (int j = 0; j < matrix.columnCount; j++) {
				matrix.data[i][j] *= value;
			}
		}
		return matrix;
	}

	// 重载乘法运算符3（矩阵与矩阵相乘）
	public static Matrix _mul(Matrix leftM, Matrix rightM) {
		if (leftM.columnCount != rightM.rowCount)
			return null;
		else {
			Matrix matrix = new Matrix(leftM.rowCount, rightM.columnCount);
			for (int i = 0; i < matrix.rowCount; i++) {
				for (int j = 0; j < matrix.columnCount; j++) {
					matrix.data[i][j] = 0;
					for (int k = 0; k < leftM.columnCount; k++) {
						matrix.data[i][j] += leftM.data[i][k]
								* rightM.data[k][j];
					}
				}
			}

			return matrix;
		}
	}

	// 重载比较运算符
	public static boolean _equal(Matrix leftM, Matrix rightM) {
		if (leftM.rowCount != rightM.rowCount
				|| leftM.columnCount != rightM.columnCount)
			return false;
		else {
			for (int i = 0; i < leftM.rowCount; i++) {
				for (int j = 0; j < leftM.columnCount; j++) {
					if (leftM.data[i][j] != rightM.data[i][j])
						return false;
				}
			}
			return true;
		}
	}

	// 重载比较运算符
	public static boolean _nequal(Matrix leftM, Matrix rightM) {
		if (leftM.rowCount != rightM.rowCount
				|| leftM.columnCount != rightM.columnCount)
			return true;
		else {
			for (int i = 0; i < leftM.rowCount; i++) {
				for (int j = 0; j < leftM.columnCount; j++) {
					if (leftM.data[i][j] != rightM.data[i][j])
						return true;
				}
			}
			return false;
		}
	}

	// / <summary>
	// / 返回指定行所有元素值
	// / </summary>
	// / <param name="rowNum">行号</param>
	// / <returns></returns>

	public double[] get(int rowNum) {
		double[] value = new double[columnCount];
		for (int i = 0; i < columnCount; i++) {
			value[i] = data[rowNum][i];
		}
		return value;
	}

	public void set(int rowNum) {
		double[] value = new double[columnCount];
		for (int i = 0; i < columnCount; i++) {
			data[rowNum][i] = value[i];
		}
	}

	// / <summary>
	// / 返回指定行列的元素的值
	// / </summary>
	// / <param name="rowNum">行号</param>
	// / <param name="colNum">列号</param>
	// / <returns></returns>
	public double get(int rowNum, int colNum) {
		return data[rowNum][colNum];
	}

	public void set(int rowNum, int colNum, double value) {
		data[rowNum][colNum] = value;
	}

	// / <summary>
	// / 矩阵的转置
	// / </summary>
	// / <returns></returns>
	public Matrix T() {
		Matrix matrix = new Matrix(columnCount, rowCount);
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				matrix.data[j][i] = data[i][j];
			}
		}

		return matrix;
	}

	// / <summary>
	// / 求矩阵的逆矩阵
	// / </summary>
	// / <returns></returns>
	public Matrix inv() {
		if (this.rowCount != this.columnCount) {
			return null;
		}
		// clone
		Matrix matrixA = new Matrix(this.rowCount, this.columnCount);
		for (int i = 0; i < matrixA.rowCount; i++) {
			for (int j = 0; j < matrixA.columnCount; j++) {
				matrixA.set(i, j, this.get(i, j));
			}
		}
		Matrix matrixB = new Matrix(matrixA.rowCount, matrixA.columnCount);
		for (int i = 0; i < matrixA.rowCount; i++) {
			for (int j = 0; j < matrixA.columnCount; j++) {
				if (i == j)
					matrixB.set(i, j, 1);
				else
					matrixB.set(i, j, 0);
			}
		}

		// i表示第几行，j表示第几列
		for (int j = 0; j < matrixA.rowCount; j++) {
			boolean flag = false;
			for (int i = j; i < matrixA.rowCount; i++) {
				if (matrixA.get(i, j) != 0) {
					flag = true;
					double temp = 0;
					// 交换i,j,两行
					if (i != j) {
						for (int k = 0; k < matrixA.rowCount; k++) {
							temp = matrixA.get(j, k);
							matrixA.set(j, k, matrixA.get(i, k));
							matrixA.set(i, k, temp);

							temp = matrixB.get(j, k);
							matrixB.set(j, k, matrixB.get(i, k));
							matrixB.set(i, k, temp);
						}
					}
					// 第j行标准化
					double d = matrixA.get(j, j);
					for (int k = 0; k < matrixA.rowCount; k++) {
						matrixA.set(j, k, matrixA.get(j, k) / d);
						matrixB.set(j, k, matrixB.get(j, k) / d);
					}
					// 消去其他行的第j列
					d = matrixA.get(i, j);
					for (int k = 0; k < matrixA.rowCount; k++) {
						if (k != j) {
							double t = matrixA.get(k, j);
							for (int n = 0; n < matrixA.rowCount; n++) {
								matrixA.set(k, n, matrixA.get(k, n) - (t / d)
										* matrixA.get(j, n));
								matrixB.set(k, n, matrixB.get(k, n) - (t / d)
										* matrixB.get(j, n));
							}
						}
					}
				}
			}
			if (!flag)
				return null;
		}
		return matrixB;
	}

	// / <summary>
	// / 将矩阵转化成字符串
	// / </summary>
	// / <returns>返回矩阵字符串</returns>
	public String ToString() {
		String str = "";
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				str += String.format("{0,8}", data[i][j]);
			}
			str += "\n";
		}
		return str;
	}

	// / <summary>
	// / 指定每个元素宽度输出矩阵字符串
	// / </summary>
	// / <param name="strWidth">元素宽度</param>
	// / <returns>返回矩阵字符串</returns>
	public String ToString(int strWidth) {
		String str = "";
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < columnCount; j++) {
				str += String.format("{0," + strWidth + "}", data[i][j]);
			}
			str += "\n";
		}
		return str;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public double[][] getData() {
		return data;
	}

	public void setData(double[][] value) {
        rowCount = value[1].length;
        columnCount = value[0].length;
        data = new double[rowCount][columnCount];
        // 赋值
        for (int i = 0; i < rowCount; i++)
        {
            for (int j = 0; j < columnCount; j++)
            {
               data[i][j] = value[i][j];
            }
        }
		//this.data = data;
	}

	public int getStrWidth() {
		return strWidth;
	}

	public void setStrWidth(int strWidth) {
		this.strWidth = strWidth;
	}

}

package edu.grinnell.csc207.util;

import java.util.Arrays;

/**
 * A class that represents a grid (matrix) with rows and columns. It can hold values and lets you
 * change the values in the grid.
 *
 * @param <T> the type of items that the grid holds
 */
public final class MatrixV0<T> implements Matrix<T> {
  /** How many columns (left to right) the grid has. */
  private int columns;

  /** How many rows (top to bottom) the grid has. */
  private int rows;

  /** The grid where we store the items. */
  private T[][] grid;

  /** The default value to put in empty spaces. */
  private T defaultValue;

  /**
   * A constructor that creates the grid with a certain number of columns and rows. It also sets a
   * default value for empty spaces.
   *
   * @param numColumns the number of columns
   * @param numRows the number of rows
   * @param defaultVal the default value for empty spaces
   */
  @SuppressWarnings("unchecked")
  public MatrixV0(int numColumns, int numRows, T defaultVal) {
    if (numColumns < 1 || numRows < 1) {
      throw new IllegalArgumentException("The grid must have at least one row and one column.");
    } // end of if statement
    this.columns = numColumns;
    this.rows = numRows;
    this.defaultValue = defaultVal;
    this.grid = (T[][]) new Object[rows][columns];
    for (int i = 0; i < rows; i++) {
      Arrays.fill(this.grid[i], defaultValue);
    } // end of for loop
  } // Constructor with default value

  /**
   * A constructor that creates the grid with a certain number of columns and rows. No default value
   * is set, so the grid starts empty.
   *
   * @param numColumns the number of columns
   * @param numRows the number of rows
   */
  public MatrixV0(int numColumns, int numRows) {
    this(numColumns, numRows, null);
  } // Constructor without default value

  /**
   * Get the value at the given row and column in the grid.
   *
   * @param row the row number
   * @param col the column number
   * @return the value at the specified position
   */
  @Override
  public T get(int row, int col) {
    checkIndex(row, col);
    return grid[row][col];
  } // get

  /**
   * Set a value in the grid at the given row and column.
   *
   * @param row the row number
   * @param col the column number
   * @param value the value to place in the grid
   */
  @Override
  public void set(int row, int col, T value) {
    checkIndex(row, col);
    grid[row][col] = value;
  } // set

  /**
   * Add a new row at a specific position in the grid.
   *
   * @param rowIndex where to insert the new row
   */
  @Override
  @SuppressWarnings("unchecked")
  public void insertRow(int rowIndex) {
    if (rowIndex < 0 || rowIndex > rows) {
      throw new IndexOutOfBoundsException("Invalid row number");
    } // end of if statement
    grid = Arrays.copyOf(grid, rows + 1);
    for (int i = rows; i > rowIndex; i--) {
      grid[i] = grid[i - 1];
    } // end of for loop
    grid[rowIndex] = (T[]) new Object[columns];
    Arrays.fill(grid[rowIndex], defaultValue);
    rows++;
  } // insertRow

  /**
   * Add a new row with specific values at a certain position in the grid.
   *
   * @param rowIndex where to insert the new row
   * @param rowValues the values to put in the new row
   * @throws ArraySizeException if the size of the row is incorrect
   */
  @Override
  public void insertRow(int rowIndex, T[] rowValues) throws ArraySizeException {
    if (rowValues.length != columns) {
      throw new ArraySizeException("Row size doesn't match the number of columns.");
    } // end of if statement
    insertRow(rowIndex);
    grid[rowIndex] = rowValues;
  } // insertRow with values

  /**
   * Add a new column at a specific position in the grid.
   *
   * @param colIndex where to insert the new column
   */
  @Override
  public void insertCol(int colIndex) {
    if (colIndex < 0 || colIndex > columns) {
      throw new IndexOutOfBoundsException("Invalid column number");
    } // end of if statement
    for (int i = 0; i < rows; i++) {
      grid[i] = Arrays.copyOf(grid[i], columns + 1);
      for (int j = columns; j > colIndex; j--) {
        grid[i][j] = grid[i][j - 1];
      } // end of for loop
      grid[i][colIndex] = defaultValue;
    } // end of for loop
    columns++;
  } // insertCol

  /**
   * Add a new column with specific values at a certain position in the grid.
   *
   * @param colIndex where to insert the new column
   * @param colValues the values to put in the new column
   * @throws ArraySizeException if the size of the column is incorrect
   */
  @Override
  public void insertCol(int colIndex, T[] colValues) throws ArraySizeException {
    if (colValues.length != rows) {
      throw new ArraySizeException("Column size doesn't match the number of rows.");
    } // end of if statement
    insertCol(colIndex);
    for (int i = 0; i < rows; i++) {
      grid[i][colIndex] = colValues[i];
    } // end of for loop
  } // insertCol with values

  /**
   * Delete a row from the grid at a certain position.
   *
   * @param rowIndex the row to remove
   */
  @Override
  public void deleteRow(int rowIndex) {
    checkRow(rowIndex);
    for (int i = rowIndex; i < rows - 1; i++) {
      grid[i] = grid[i + 1];
    } // end of for loop
    rows--;
    grid = Arrays.copyOf(grid, rows);
  } // deleteRow

  /**
   * Delete a column from the grid at a certain position.
   *
   * @param colIndex the column to remove
   */
  @Override
  public void deleteCol(int colIndex) {
    checkCol(colIndex);
    for (int i = 0; i < rows; i++) {
      for (int j = colIndex; j < columns - 1; j++) {
        grid[i][j] = grid[i][j + 1];
      } // end of for loop
      grid[i] = Arrays.copyOf(grid[i], columns - 1);
    } // end of for loop
    columns--;
  } // deleteCol

  /**
   * Fill a part of the grid with a certain value.
   *
   * @param startRow the starting row (inclusive)
   * @param startCol the starting column (inclusive)
   * @param endRow the ending row (exclusive)
   * @param endCol the ending column (exclusive)
   * @param value the value to fill in the grid
   */
  @Override
  public void fillRegion(int startRow, int startCol, int endRow, int endCol, T value) {
    for (int i = startRow; i < endRow; i++) {
      for (int j = startCol; j < endCol; j++) {
        set(i, j, value);
      } // end of for loop
    } // end of for loop
  } // fillRegion

  /**
   * Fill a line in the grid with a certain value.
   *
   * @param startRow the starting row
   * @param startCol the starting column
   * @param rowStep the row step size (how many rows to move down each time)
   * @param colStep the column step size (how many columns to move right each time)
   * @param endRow the ending row
   * @param endCol the ending column
   * @param value the value to fill in the grid
   */
  @Override
  public void fillLine(
      int startRow, int startCol, int rowStep, int colStep, int endRow, int endCol, T value) {
    int currentRow = startRow;
    int currentCol = startCol;
    while (currentRow < endRow && currentCol < endCol) {
      set(currentRow, currentCol, value);
      currentRow += rowStep;
      currentCol += colStep;
    } // end of while loop
  } // fillLine

  // Check if the row and column are valid
  private void checkIndex(int rowIndex, int colIndex) {
    if (rowIndex < 0 || rowIndex >= rows || colIndex < 0 || colIndex >= columns) {
      throw new IndexOutOfBoundsException("Invalid row or column number.");
    } // end of if statement
  } // checkIndex

  // Check if the row is valid
  private void checkRow(int rowIndex) {
    if (rowIndex < 0 || rowIndex >= rows) {
      throw new IndexOutOfBoundsException("Invalid row number.");
    } // end of if statement
  } // checkRow

  // Check if the column is valid
  private void checkCol(int colIndex) {
    if (colIndex < 0 || colIndex >= columns) {
      throw new IndexOutOfBoundsException("Invalid column number.");
    } // end of if statement
  } // checkCol

  /**
   * Make a copy of the grid.
   *
   * @return a copy of this grid
   */
  @Override
  public MatrixV0<T> clone() {
    MatrixV0<T> copy = new MatrixV0<>(columns, rows, defaultValue);
    for (int i = 0; i < rows; i++) {
      copy.grid[i] = Arrays.copyOf(this.grid[i], this.columns);
    } // end of for loop
    return copy;
  } // clone

  /**
   * Check if two grids are the same.
   *
   * @param obj the other object to compare
   * @return true if the grids are the same, false otherwise
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    } // end of if statement
    if (!(obj instanceof MatrixV0)) {
      return false;
    } // end of if statement
    MatrixV0<?> other = (MatrixV0<?>) obj;
    return this.columns == other.columns
        && this.rows == other.rows
        && Arrays.deepEquals(this.grid, other.grid);
  } // equals

  /**
   * Returns a hash code for the grid.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Arrays.deepHashCode(grid);
  } // hashCode

  /**
   * Get the number of columns in the grid.
   *
   * @return the number of columns
   */
  @Override
  public int width() {
    return columns;
  } // width

  /**
   * Get the number of rows in the grid.
   *
   * @return the number of rows
   */
  @Override
  public int height() {
    return rows;
  } // height
} // MatrixV0

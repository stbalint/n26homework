package com.example.statistics.pojo;

public final class TransactionStatistics {

	private final double sum;

	private final double avg;

	private final double max;

	private final double min;

	private final long count;

	private TransactionStatistics(double sum, double avg, double max, double min, long count) {
		super();
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}

	public double getSum() {
		return sum;
	}

	public double getAvg() {
		return avg;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public long getCount() {
		return count;
	}

	public static class Builder {
		private double sum = 0;

		private double avg = 0;

		private double max = 0;

		private double min = 0;

		private long count = 0;

		public Builder setSum(double sum) {
			this.sum = sum;
			return this;
		}

		public Builder setAvg(double avg) {
			this.avg = avg;
			return this;
		}

		public Builder setMax(double max) {
			this.max = max;
			return this;
		}

		public Builder setMin(double min) {
			this.min = min;
			return this;
		}

		public Builder setCount(long count) {
			this.count = count;
			return this;
		}

		public TransactionStatistics build() {
			return new TransactionStatistics(sum, avg, max, min, count);
		}
	}
}

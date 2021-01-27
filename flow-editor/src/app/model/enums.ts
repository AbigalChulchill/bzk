

export enum ChronoUnit {
  /**
   * Unit that represents the concept of a nanosecond, the smallest supported unit of time.
   * For the ISO calendar system, it is equal to the 1,000,000,000th part of the second unit.
   */
  NANOS = 'NANOS',
  /**
   * Unit that represents the concept of a microsecond.
   * For the ISO calendar system, it is equal to the 1,000,000th part of the second unit.
   */
  MICROS = 'MICROS',
  /**
   * Unit that represents the concept of a millisecond.
   * For the ISO calendar system, it is equal to the 1000th part of the second unit.
   */
  MILLIS = 'MILLIS',
  /**
   * Unit that represents the concept of a second.
   * For the ISO calendar system, it is equal to the second in the SI system
   * of units, except around a leap-second.
   */
  SECONDS = 'SECONDS',
  /**
   * Unit that represents the concept of a minute.
   * For the ISO calendar system, it is equal to 60 seconds.
   */
  MINUTES = 'MINUTES',
  /**
   * Unit that represents the concept of an hour.
   * For the ISO calendar system, it is equal to 60 minutes.
   */
  HOURS = 'HOURS',
  /**
   * Unit that represents the concept of half a day, as used in AM/PM.
   * For the ISO calendar system, it is equal to 12 hours.
   */
  HALF_DAYS = 'HALF_DAYS',
  /**
   * Unit that represents the concept of a day.
   * For the ISO calendar system, it is the standard day from midnight to midnight.
   * The estimated duration of a day is {@code 24 Hours}.
   * <p>
   * When used with other calendar systems it must correspond to the day defined by
   * the rising and setting of the Sun on Earth. It is not required that days begin
   * at midnight - when converting between calendar systems, the date should be
   * equivalent at midday.
   */
  DAYS = 'DAYS',
  /**
   * Unit that represents the concept of a week.
   * For the ISO calendar system, it is equal to 7 days.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days.
   */
  WEEKS = 'WEEKS',
  /**
   * Unit that represents the concept of a month.
   * For the ISO calendar system, the length of the month varies by month-of-year.
   * The estimated duration of a month is one twelfth of {@code 365.2425 Days}.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days.
   */
  MONTHS = 'MONTHS',
  /**
   * Unit that represents the concept of a year.
   * For the ISO calendar system, it is equal to 12 months.
   * The estimated duration of a year is {@code 365.2425 Days}.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * or months roughly equal to a year defined by the passage of the Earth around the Sun.
   */
  YEARS = 'YEARS',
  /**
   * Unit that represents the concept of a decade.
   * For the ISO calendar system, it is equal to 10 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  DECADES = 'DECADES',
  /**
   * Unit that represents the concept of a century.
   * For the ISO calendar system, it is equal to 100 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  CENTURIES = 'CENTURIES',
  /**
   * Unit that represents the concept of a millennium.
   * For the ISO calendar system, it is equal to 1000 years.
   * <p>
   * When used with other calendar systems it must correspond to an integral number of days
   * and is normally an integral number of years.
   */
  MILLENNIA = 'MILLENNIA',
  /**
   * Unit that represents the concept of an era.
   * The ISO calendar system doesn't have eras thus it is impossible to add
   * an era to a date or date-time.
   * The estimated duration of the era is artificially defined as {@code 1,000,000,000 Years}.
   * <p>
   * When used with other calendar systems there are no restrictions on the unit.
   */
  ERAS = 'ERAS',
  /**
   * Artificial unit that represents the concept of forever.
   * This is primarily used with {@link TemporalField} to represent unbounded fields
   * such as the year or era.
   * The estimated duration of this unit is artificially defined as the largest duration
   * supported by {@link Duration}.
   */
  FOREVER = 'FOREVER'
}

export enum TimeUnit {
  /**
   * Time unit representing one thousandth of a microsecond.
   */
  NANOSECONDS = 'NANOSECONDS',
  /**
   * Time unit representing one thousandth of a millisecond.
   */
  MICROSECONDS = 'MICROSECONDS',
  /**
   * Time unit representing one thousandth of a second.
   */
  MILLISECONDS = 'MILLISECONDS',
  /**
   * Time unit representing one second.
   */
  SECONDS = 'SECONDS',
  /**
   * Time unit representing sixty seconds.
   * @since 1.6
   */
  MINUTES = 'MINUTES',
  /**
   * Time unit representing sixty minutes.
   * @since 1.6
   */
  HOURS = 'HOURS',
  /**
   * Time unit representing twenty four hours.
   * @since 1.6
   */
  DAYS = 'DAYS'

}

export enum HttpMethod {
  GET = 'GET', HEAD = 'HEAD', POST = 'POST', PUT = 'PUT', PATCH = 'PATCH', DELETE = 'DELETE', OPTIONS = 'OPTIONS', TRACE = 'TRACE'
}

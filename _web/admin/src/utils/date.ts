// 获取常用时间
import dayjs from 'dayjs';

export const LAST_7_DAYS = [
  dayjs().subtract(7, 'day').format('YYYY-MM-DD'),
  dayjs().subtract(1, 'day').format('YYYY-MM-DD'),
];

export const LAST_30_DAYS = [
  dayjs().subtract(30, 'day').format('YYYY-MM-DD'),
  dayjs().subtract(1, 'day').format('YYYY-MM-DD'),
];

/**
 * 按指定格式格式化时间
 * @param date 需要格式化的时间（Date对象、字符串或dayjs对象）
 * @param format 格式字符串，默认 'YYYY-MM-DD'
 * @returns 格式化后的字符串
 */
export function formatDate(date: string | Date | dayjs.Dayjs, format: string = 'YYYY-MM-DD'): string {
  return dayjs(date).format(format);
}

/**
 * 根据日期返回中文星期
 * @param date 需要判断的日期（Date对象、字符串或dayjs对象）
 * @returns 中文星期，如 '星期一'
 */
export function getWeekday(date: string | Date | dayjs.Dayjs): string {
  const weekMap = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
  return weekMap[dayjs(date).day()];
}

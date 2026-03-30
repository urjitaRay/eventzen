import { type Event } from "./Events";

export type Expense = {
  expenseId: number;

  event: Event;

  amount: number;
  description: string;

  paymentStatus: "PAID" | "PENDING";

  expenseDate: string;
};
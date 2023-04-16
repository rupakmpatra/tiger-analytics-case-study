export interface ApiResponse<T> {
    data: T;
    status: string;
    message: string;
    total: number;
    totalPages: number;
    pageNum: number;
  }
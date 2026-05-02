export interface Product {
  id: number;
  nome: string;
  descricao?: string;
  preco: number;
  dataCadastro: string;
}

export interface ProductPayload {
  nome: string;
  descricao?: string;
  preco: number;
}

export type TaskStatus = "PENDENTE" | "EM_ANDAMENTO" | "CONCLUIDA";

export interface Task {
  id: number;
  titulo: string;
  descricao?: string;
  status: TaskStatus;
  createdAt: string;
  updatedAt: string;
}

export interface TaskPayload {
  titulo?: string;
  descricao?: string;
  status?: TaskStatus;
}

export interface ApiResponse<T> {
  success: boolean;
  data: T;
  message: string;
}

export interface AuthResponse {
  token: string;
  tipo: string;
}

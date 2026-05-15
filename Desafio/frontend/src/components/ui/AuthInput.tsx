import React, { InputHTMLAttributes } from 'react';

export type AuthInputProps = InputHTMLAttributes<HTMLInputElement> & {
  label: string;
  error?: string;
};

export function AuthInput({ label, error, ...props }: AuthInputProps) {
  return (
    <label className="field">
      <span>{label}</span>
      <input {...props} />
      {error && <small>{error}</small>}
    </label>
  );
}
export function InlineError({ message }: { message: string }) {
  return <div className="inline-error">{message}</div>;
}

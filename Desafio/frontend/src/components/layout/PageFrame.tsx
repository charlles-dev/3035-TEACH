import { PropsWithChildren, ReactNode } from "react";
import { BottomNav } from "./BottomNav";

export function PageFrame({ title, children, action }: PropsWithChildren<{ title: string; action?: ReactNode }>) {
  return (
    <section className="page-frame">
      <header className="page-header">
        <div>
          <p className="eyebrow">TeachGram</p>
          <h1>{title}</h1>
        </div>
        {action}
      </header>
      {children}
      <BottomNav />
    </section>
  );
}

import { Home, Search, PlusSquare, Bell, Settings } from "lucide-react";
import { NavLink } from "react-router-dom";

export function BottomNav() {
  return (
    <nav className="bottom-nav" aria-label="Navegacao principal">
      <NavLink to="/"><Home size={21} /></NavLink>
      <NavLink to="/search"><Search size={21} /></NavLink>
      <NavLink to="/new-post"><PlusSquare size={23} /></NavLink>
      <NavLink to="/notifications"><Bell size={21} /></NavLink>
      <NavLink to="/settings/account"><Settings size={21} /></NavLink>
    </nav>
  );
}

import { Link } from "lucide-react";

export function TeachgramLogo() {
  return (
    <Link to="/" className="logo" aria-label="TeachGram">
      <svg className="logo-mark" viewBox="0 0 40 40" fill="none" xmlns="http://www.w3.org/2000/svg">
        <circle cx="20" cy="20" r="20" fill="url(#paint0_linear)" />
        <path d="M25.5 16C27.9853 16 30 18.0147 30 20.5C30 22.9853 27.9853 25 25.5 25C23.0147 25 21 22.9853 21 20.5C21 18.0147 23.0147 16 25.5 16Z" stroke="white" strokeWidth="2.5"/>
        <path d="M14.5 16C16.9853 16 19 18.0147 19 20.5C19 22.9853 16.9853 25 14.5 25C12.0147 25 10 22.9853 10 20.5C10 18.0147 12.0147 16 14.5 16Z" stroke="white" strokeWidth="2.5"/>
        <path d="M19 20.5H21" stroke="white" strokeWidth="2.5" strokeLinecap="round"/>
        <defs>
          <linearGradient id="paint0_linear" x1="0" y1="0" x2="40" y2="40" gradientUnits="userSpaceOnUse">
            <stop stopColor="#FA874C" />
            <stop offset="1" stopColor="#E94F74" />
          </linearGradient>
        </defs>
      </svg>
      <span style={{ fontFamily: "Georgia, serif", fontWeight: 600, letterSpacing: "-0.5px", fontSize: "28px" }}>Teachgram</span>
    </Link>
  );
}

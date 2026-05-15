import { Link } from "react-router-dom";
import { figmaAssets } from "../../assets/figma/figmaAssets";

export function TeachgramLogo() {
  return (
    <Link to="/" className="logo" aria-label="TeachGram">
      <span className="logo-mark" aria-hidden>
        <img className="logo-mark-bg" src={figmaAssets.logoMark} alt="" />
        <img className="logo-mark-glyph" src={figmaAssets.logoGlyph} alt="" />
      </span>
      <img className="logo-wordmark" src={figmaAssets.logoWordmark} alt="" />
    </Link>
  );
}

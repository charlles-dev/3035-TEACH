import {
  BrowserRouter,
  Navigate,
  Route,
  Routes,
} from "react-router-dom";
import { AppProviders } from "./AppProviders";
import { ProtectedShell } from "./components/layout/ProtectedShell";
import { AiStudioPage } from "./pages/AiStudioPage";
import { ConnectionsPage } from "./pages/ConnectionsPage";
import { DeleteAccountPage } from "./pages/DeleteAccountPage";
import { DemoPage } from "./pages/DemoPage";
import { FeedPage } from "./pages/FeedPage";
import { ForgotPasswordPage } from "./pages/ForgotPasswordPage";
import { LoginPage } from "./pages/LoginPage";
import { NewPostPage } from "./pages/NewPostPage";
import { NotificationsPage } from "./pages/NotificationsPage";
import { PostDetailPage } from "./pages/PostDetailPage";
import { ProfilePage } from "./pages/ProfilePage";
import { SavedPage } from "./pages/SavedPage";
import { SearchPage } from "./pages/SearchPage";
import { SettingsAccountPage } from "./pages/SettingsAccountPage";
import { SettingsProfilePage } from "./pages/SettingsProfilePage";
import { SignupPage } from "./pages/SignupPage";

export function App() {
  return (
    <AppProviders>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/demo" element={<DemoPage />} />
          <Route element={<ProtectedShell />}>
            <Route path="/" element={<FeedPage />} />
            <Route path="/new-post" element={<NewPostPage />} />
            <Route path="/posts/:postId" element={<PostDetailPage />} />
            <Route path="/posts/:postId/edit" element={<NewPostPage editing />} />
            <Route path="/friends" element={<ConnectionsPage />} />
            <Route path="/relationships/add" element={<SearchPage relationshipMode />} />
            <Route path="/u/:username" element={<ProfilePage />} />
            <Route path="/settings" element={<SettingsAccountPage />} />
            <Route path="/settings/account" element={<SettingsAccountPage />} />
            <Route path="/settings/profile" element={<SettingsProfilePage />} />
            <Route path="/settings/delete" element={<DeleteAccountPage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/notifications" element={<NotificationsPage />} />
            <Route path="/saved" element={<SavedPage />} />
            <Route path="/ai/studio" element={<AiStudioPage />} />
            <Route path="/profile-photo" element={<SettingsProfilePage photoFocus />} />
          </Route>
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AppProviders>
  );
}

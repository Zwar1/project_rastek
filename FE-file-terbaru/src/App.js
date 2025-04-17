import { BrowserRouter, Route, Routes } from "react-router-dom";

import EmployeePage from "./pages/EmployeePage";
import LeavePage from "./pages/LeaveRequestPage";
import AddLeave from "./pages/AddLeavePage";
import AddCutiType from "./pages/AddCutiTypePage";
import Leads from "./pages/LeadsPage";
import ClientDataPage from "./pages/ClientData/ClientDataPage";
import ProjectPage from "./pages/ProjectPage";
import AttendancePage from "./pages/AttendancePage";
import ProfilePage from "./pages/ViewProfilePage";
import TaskPage from "./pages/TaskPage";
import RolesPage from "./pages/RolesPage";
import MyLeavePage from "./pages/MyLeavePage";
import AddProject from "./pages/AddProjectPage";
import ProjectDetailsPage from "./pages/ProjectDetailsPage";
import FinancePage from "./pages/FinancePage";
import AccountsPage from "./pages/AccountsPage";
import DepositPage from "./pages/DepositPage";
import ExpensesPage from "./pages/ExpensesPage";
import TransactionPage from "./pages/TransactionPage";
import EmployeePdfPage from "./pages/EmployeePdfPage";
import MonthlyReportPDFPage from "./pages/MonthlyReportPDFPage";
import EmployeeAttendancePage from "./pages/EmployeeAttendancePage";
import ClientEditPage from "./pages/ClientData/ClientEditPage";

import Dashboard from "./pages/Dashboard";
import EmailPage from "./pages/CompanyManagementPage";
import AddUserPage from "./pages/AddUserPage";
import AlertPage from "./pages/AlertPage";
import AssignRolePage from "./pages/AssignRolePage";
import AvatarPage from "./pages/AvatarPage";
import BadgesPage from "./pages/BadgesPage";
import ButtonPage from "./pages/ButtonPage";
import CalendarMainPage from "./pages/CalendarMainPage";
import CardPage from "./pages/CardPage";
import CarouselPage from "./pages/CarouselPage";
import ChatEmptyPage from "./pages/ChatEmptyPage";
import ChatMessagePage from "./pages/ChatMessagePage";
import ChatProfilePage from "./pages/ChatProfilePage";
import CodeGeneratorNewPage from "./pages/CodeGeneratorNewPage";
import CodeGeneratorPage from "./pages/CodeGeneratorPage";
import ColorsPage from "./pages/ColorsPage";
import ColumnChartPage from "./pages/ColumnChartPage";
import CompanyPage from "./pages/CompanyPage";
import CurrenciesPage from "./pages/CurrenciesPage";
import DropdownPage from "./pages/DropdownPage";
import ErrorPage from "./pages/ErrorPage";
import FaqPage from "./pages/FaqPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import FormLayoutPage from "./pages/FormLayoutPage";
import FormValidationPage from "./pages/FormValidationPage";
import FormPage from "./pages/FormPage";
import GalleryPage from "./pages/GalleryPage";
import ImageGeneratorPage from "./pages/ImageGeneratorPage";
import ImageUploadPage from "./pages/ImageUploadPage";
import InvoiceAddPage from "./pages/InvoiceAddPage";
import InvoiceEditPage from "./pages/InvoiceEditPage";
import InvoiceListPage from "./pages/InvoiceListPage";
import InvoicePreviewPage from "./pages/InvoicePreviewPage";
import KanbanPage from "./pages/KanbanPage";
import LanguagePage from "./pages/LanguagePage";
import LineChartPage from "./pages/LineChartPage";
import ListPage from "./pages/ListPage";
import MarketplaceDetailsPage from "./pages/MarketplaceDetailsPage";
import MarketplacePage from "./pages/MarketplacePage";
import NotificationAlertPage from "./pages/NotificationAlertPage";
import NotificationPage from "./pages/NotificationPage";
import PaginationPage from "./pages/PaginationPage";
import PaymentGatewayPage from "./pages/PaymentGatewayPage";
import PieChartPage from "./pages/PieChartPage";
import PortfolioPage from "./pages/PortfolioPage";
import PricingPage from "./pages/PricingPage";
import ProgressPage from "./pages/ProgressPage";
import RadioPage from "./pages/RadioPage";
import RoleAccessPage from "./pages/RoleAccessPage";
import SignInPage from "./pages/SignInPage";
import SignUpPage from "./pages/SignUpPage";
import StarRatingPage from "./pages/StarRatingPage";
import SwitchPage from "./pages/SwitchPage";
import TableBasicPage from "./pages/TableBasicPage";
import TableDataPage from "./pages/TableDataPage";
import TabsPage from "./pages/TabsPage";
import TagsPage from "./pages/TagsPage";
import TermsConditionPage from "./pages/TermsConditionPage";
import TextGeneratorPage from "./pages/TextGeneratorPage";
import ThemePage from "./pages/ThemePage";
import TooltipPage from "./pages/TooltipPage";
import TypographyPage from "./pages/TypographyPage";
import UsersGridPage from "./pages/UsersGridPage";
import UsersListPage from "./pages/UsersListPage";
import ViewDetailsPage from "./pages/ViewDetailsPage";
import VideoGeneratorPage from "./pages/VideoGeneratorPage";
import VideosPage from "./pages/VideosPage";
import ViewProfilePage from "./pages/ViewProfilePage";
import VoiceGeneratorPage from "./pages/VoiceGeneratorPage";
import WalletPage from "./pages/WalletPage";
import WidgetsPage from "./pages/WidgetsPage";
import WizardPage from "./pages/WizardPage";
import RouteScrollToTop from "./helper/RouteScrollToTop";
import TextGeneratorNewPage from "./pages/TextGeneratorNewPage";
import AddTaskPage from "./pages/AddTaskPage";
import AddEmployeePage from "./pages/AddEmployeePage";

function App() {
  return (
    <BrowserRouter>
      <RouteScrollToTop />
      <Routes>
        <Route exact path="/" element={<SignInPage />} />
        <Route exact path="/dashboard" element={<Dashboard />} />

        {/* SL */}
        <Route exact path="/employees" element={<EmployeePage />} />
        <Route exact path="/leaves" element={<LeavePage />} />
        <Route exact path="/add-leaves" element={<AddLeave />} />
        <Route exact path="/add-cuti-type" element={<AddCutiType />} />
        <Route exact path="/leads" element={<Leads />} />
        <Route exact path="/client" element={<ClientDataPage />} />
        <Route exact path="/client-detail" element={<ClientEditPage />} />
        <Route exact path="/project" element={<ProjectPage />} />
        <Route exact path="/attendance" element={<AttendancePage />} />
        <Route exact path="/profile" element={<ProfilePage />} />
        <Route exact path="/task" element={<TaskPage />} />
        <Route exact path="/roles" element={<RolesPage />} />
        <Route exact path="/my-leave" element={<MyLeavePage />} />
        <Route exact path="/add-project" element={<AddProject />} />
        <Route exact path="/project-details" element={<ProjectDetailsPage />} />

        <Route exact path="/employee-pdf/:id" element={<EmployeePdfPage />} />

        <Route exact path="/finance-page" element={<FinancePage />} />
        <Route exact path="/AccountsPage" element={<AccountsPage />} />
        <Route exact path="/DepositPage" element={<DepositPage />} />
        <Route exact path="/ExpensesPage" element={<ExpensesPage />} />
        <Route exact path="/TransactionPage" element={<TransactionPage />} />
        {/* <Route exact path="/task/:id" element={<TaskDetailPage />} /> */}

        <Route exact path="/addtaskpage" element={<AddTaskPage />} />
        <Route exact path="/add-user" element={<AddUserPage />} />
        <Route exact path="/alert" element={<AlertPage />} />
        <Route exact path="/AddEmployee" element={<AddEmployeePage />} />
        <Route exact path="/assign-role" element={<AssignRolePage />} />
        <Route exact path="/avatar" element={<AvatarPage />} />
        <Route exact path="/badges" element={<BadgesPage />} />
        <Route exact path="/button" element={<ButtonPage />} />
        <Route exact path="/calendar-main" element={<CalendarMainPage />} />
        <Route exact path="/calendar" element={<CalendarMainPage />} />
        <Route exact path="/card" element={<CardPage />} />
        <Route exact path="/carousel" element={<CarouselPage />} />
        <Route exact path="/chat-empty" element={<ChatEmptyPage />} />
        <Route exact path="/chat-message" element={<ChatMessagePage />} />
        <Route exact path="/chat-profile" element={<ChatProfilePage />} />
        <Route exact path="/code-generator" element={<CodeGeneratorPage />} />
        <Route
          exact
          path="/code-generator-new"
          element={<CodeGeneratorNewPage />}
        />
        <Route exact path="/colors" element={<ColorsPage />} />
        <Route exact path="/column-chart" element={<ColumnChartPage />} />
        <Route exact path="/company" element={<CompanyPage />} />
        <Route exact path="/currencies" element={<CurrenciesPage />} />
        <Route exact path="/dropdown" element={<DropdownPage />} />
        <Route exact path="/email" element={<EmailPage />} />
        <Route exact path="/faq" element={<FaqPage />} />
        <Route exact path="/forgot-password" element={<ForgotPasswordPage />} />
        <Route exact path="/form-layout" element={<FormLayoutPage />} />
        <Route exact path="/form-validation" element={<FormValidationPage />} />
        <Route exact path="/form" element={<FormPage />} />
        <Route exact path="/gallery" element={<GalleryPage />} />
        <Route exact path="/image-generator" element={<ImageGeneratorPage />} />
        <Route exact path="/image-upload" element={<ImageUploadPage />} />
        <Route exact path="/invoice-add" element={<InvoiceAddPage />} />
        <Route exact path="/invoice-edit" element={<InvoiceEditPage />} />
        <Route exact path="/invoice-list" element={<InvoiceListPage />} />
        <Route exact path="/invoice-preview" element={<InvoicePreviewPage />} />
        <Route exact path="/kanban" element={<KanbanPage />} />
        <Route exact path="/language" element={<LanguagePage />} />
        <Route exact path="/line-chart" element={<LineChartPage />} />
        <Route exact path="/list" element={<ListPage />} />
        <Route
          exact
          path="/marketplace-details"
          element={<MarketplaceDetailsPage />}
        />
        <Route exact path="/marketplace" element={<MarketplacePage />} />
        <Route
          exact
          path="/notification-alert"
          element={<NotificationAlertPage />}
        />
        <Route exact path="/notification" element={<NotificationPage />} />
        <Route exact path="/pagination" element={<PaginationPage />} />
        <Route exact path="/payment-gateway" element={<PaymentGatewayPage />} />
        <Route exact path="/pie-chart" element={<PieChartPage />} />
        <Route exact path="/portfolio" element={<PortfolioPage />} />
        <Route exact path="/pricing" element={<PricingPage />} />
        <Route exact path="/progress" element={<ProgressPage />} />
        <Route exact path="/radio" element={<RadioPage />} />
        <Route exact path="/role-access" element={<RoleAccessPage />} />
        <Route exact path="/" element={<SignInPage />} />
        <Route exact path="/sign-up" element={<SignUpPage />} />
        <Route exact path="/star-rating" element={<StarRatingPage />} />
        <Route exact path="/switch" element={<SwitchPage />} />
        <Route exact path="/TaskPage" element={<TaskPage />} />
        <Route exact path="/table-basic" element={<TableBasicPage />} />
        <Route exact path="/table-data" element={<TableDataPage />} />
        <Route exact path="/tabs" element={<TabsPage />} />
        <Route exact path="/tags" element={<TagsPage />} />
        <Route exact path="/terms-condition" element={<TermsConditionPage />} />
        <Route
          exact
          path="/text-generator-new"
          element={<TextGeneratorNewPage />}
        />
        <Route exact path="/text-generator" element={<TextGeneratorPage />} />
        <Route exact path="/theme" element={<ThemePage />} />
        <Route exact path="/tooltip" element={<TooltipPage />} />
        <Route exact path="/typography" element={<TypographyPage />} />
        <Route exact path="/users-grid" element={<UsersGridPage />} />
        <Route exact path="/users-list" element={<UsersListPage />} />
        <Route exact path="/view-details" element={<ViewDetailsPage />} />
        <Route exact path="/video-generator" element={<VideoGeneratorPage />} />
        <Route exact path="/videos" element={<VideosPage />} />
        <Route exact path="/view-profile" element={<ViewProfilePage />} />
        <Route exact path="/voice-generator" element={<VoiceGeneratorPage />} />
        <Route exact path="/wallet" element={<WalletPage />} />
        <Route exact path="/widgets" element={<WidgetsPage />} />
        <Route exact path="/wizard" element={<WizardPage />} />
        <Route exact path="/detailed-info" element={<EmployeePdfPage />} />
        <Route
          exact
          path="/detailed-report"
          element={<MonthlyReportPDFPage />}
        />
        <Route
          exact
          path="/detailed-employee"
          element={<EmployeeAttendancePage />}
        />

        <Route exact path="*" element={<ErrorPage />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
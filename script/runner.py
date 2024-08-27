import sys
import datetime
from qt_material import apply_stylesheet
from PyQt5.QtWidgets import QApplication, QMainWindow, QWidget, QVBoxLayout, QHBoxLayout, QLabel, QPushButton, QScrollArea, QFrame
from PyQt5.QtGui import QFont, QColor, QBrush, QPalette
from PyQt5.QtCore import Qt


class CustomButton(QPushButton):
    def __init__(self, text, parent=None):
        super().__init__(text, parent)


class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.run_flag = True
        self.run_survive_flag = True
        self.initUI()

    def initUI(self):
        # 创建主窗口
        self.setWindowTitle("服务器管理")
        self.setGeometry(100, 100, 800, 600)

        # 创建主布局
        main_layout = QHBoxLayout()

        # 创建左侧布局
        left_layout = QVBoxLayout()
        left_layout.setContentsMargins(20, 0, 20, 20)  # 调整上间距为 0

        # 添加左侧组件

        button_layout = QVBoxLayout()
        button_layout.addSpacing(20)
        btn_start_server = CustomButton("启动服务器")
        btn_stop_server = CustomButton("停止服务器")
        btn_start_tunnel = CustomButton("启动内网穿透")
        btn_stop_tunnel = CustomButton("停止内网穿透")
        btn_start_server.clicked.connect(self.start_server)
        btn_stop_server.clicked.connect(self.stop_server)
        btn_start_tunnel.clicked.connect(self.start_tunnel)
        btn_stop_tunnel.clicked.connect(self.stop_tunnel)
        button_layout.addWidget(btn_start_server)
        button_layout.addWidget(btn_stop_server)
        button_layout.addWidget(btn_start_tunnel)
        button_layout.addWidget(btn_stop_tunnel)
        button_layout.addStretch()
        left_layout.addLayout(button_layout)

        status_frame = QFrame()
        status_layout = QVBoxLayout()
        status_layout.setContentsMargins(10, 5, 10, 5)  # 调整内间距
        self.run_flag_label = QLabel(f"启动状态：{'开启' if self.run_flag else '关闭'}")
        self.run_flag_label.setAlignment(Qt.AlignVCenter)  # 垂直居中
        self.run_survive_flag_label = QLabel(
            f"生命探测：{'成功' if self.run_survive_flag else '失败'}")
        self.run_survive_flag_label.setAlignment(Qt.AlignVCenter)  # 垂直居中
        status_layout.addWidget(self.run_flag_label)
        status_layout.addWidget(self.run_survive_flag_label)
        status_frame.setLayout(status_layout)
        left_layout.addWidget(status_frame)

        # 创建右侧布局
        right_layout = QVBoxLayout()

        # 添加右侧组件
        self.log_area = QScrollArea()
        self.log_area.setWidgetResizable(True)
        log_widget = QWidget()
        self.log_layout = QVBoxLayout()
        self.log_layout.setSpacing(5)  # 调整日志信息行间距
        self.log_layout.addStretch()
        log_widget.setLayout(self.log_layout)
        self.log_area.setWidget(log_widget)
        right_layout.addWidget(self.log_area)

        # 将左右布局添加到主布局
        main_layout.addLayout(left_layout, 2)
        main_layout.addLayout(right_layout, 3)

        # 创建主窗口并设置背景颜色
        central_widget = QWidget()
        central_widget.setLayout(main_layout)
        self.setCentralWidget(central_widget)
        # self.setStyleSheet("background-color: white;")

    def start_server(self):
        self.run_flag = True
        self.run_flag_label.setText(f"启动状态：{'开启' if self.run_flag else '关闭'}")
        self.add_log("服务器启动成功")

    def stop_server(self):
        self.run_flag = False
        self.run_flag_label.setText(f"启动状态：{'开启' if self.run_flag else '关闭'}")
        self.add_log("服务器停止")

    def start_tunnel(self):
        self.run_survive_flag = True
        self.run_survive_flag_label.setText(
            f"生命探测：{'成功' if self.run_survive_flag else '失败'}")
        self.add_log("内网穿透建立连接")

    def stop_tunnel(self):
        self.run_survive_flag = False
        self.run_survive_flag_label.setText(
            f"生命探测：{'成功' if self.run_survive_flag else '失败'}")
        self.add_log("内网穿透连接断开")

    def add_log(self, content):
        timestamp = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        timestamp_label = QLabel(timestamp)
        timestamp_label.setAlignment(Qt.AlignLeft)
        content_label = QLabel(content)
        content_label.setAlignment(Qt.AlignLeft)

        # content_label.setStyleSheet("QLabel { padding: 0px; margin: 0px; }")
        content_label.setContentsMargins(0, 0, 0, 0)
        # timestamp_label.setStyleSheet("QLabel { padding: 0px; margin: 0px; }")
        timestamp_label.setContentsMargins(0, 0, 0, 0)

        log_layout = QVBoxLayout()
        # Set the spacing between timestamp and content
        log_layout.setSpacing(0)
        log_layout.setContentsMargins(0, 0, 0, 0)
        log_layout.addWidget(timestamp_label)
        log_layout.addWidget(content_label)

        log_widget = QWidget()
        log_widget.setLayout(log_layout)
        self.log_layout.addWidget(log_widget)

        self.log_area.verticalScrollBar().setValue(
            self.log_area.verticalScrollBar().maximum())


if __name__ == "__main__":
    app = QApplication(sys.argv)
    window = MainWindow()
    apply_stylesheet(app, theme='dark_teal.xml')
    window.show()
    sys.exit(app.exec_())

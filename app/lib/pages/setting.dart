import 'package:app/pages/login.dart';
import 'package:flutter/material.dart';

class SettingPage extends StatelessWidget {
  const SettingPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('设置'),
      ),
      body: ListView(
        children: [
          _buildSection('账号', [
            _buildMenuItem('账号与安全', Icons.person_outline),
            _buildMenuItem('隐私设置', Icons.lock_outline),
          ]),
          _buildSection('通用', [
            _buildMenuItem('通知设置', Icons.notifications_none),
            _buildMenuItem('通用设置', Icons.settings_outlined),
            _buildMenuItem('背景设置', Icons.palette_outlined),
            _buildMenuItem('长截模式', Icons.crop_outlined),
            _buildMenuItem('字体大小', Icons.text_fields),
          ]),
          _buildSection('关于', [
            _buildMenuItem('反馈与帮助', Icons.help_outline),
            _buildMenuItem('7解与管理广告渠道', Icons.campaign_outlined),
            _buildMenuItem('抖音规则中心', Icons.policy_outlined),
            _buildMenuItem('清除缓存', Icons.cleaning_services_outlined),
          ]),
          const SizedBox(height: 20),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 16),
            child: ElevatedButton(
              onPressed: () {
                Navigator.pushReplacement(context,
                    MaterialPageRoute(builder: (context) => const LoginPage()));
              },
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.red,
                foregroundColor: Colors.white,
                padding: const EdgeInsets.symmetric(vertical: 12),
              ),
              child: const Text(
                '退出登录',
                style: TextStyle(fontSize: 16),
              ),
            ),
          ),
          const SizedBox(height: 20),
        ],
      ),
    );
  }

  Widget _buildSection(String title, List<Widget> children) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
          child: Text(
            title,
            style: const TextStyle(
              fontSize: 14,
              color: Colors.grey,
            ),
          ),
        ),
        ...children,
      ],
    );
  }

  Widget _buildMenuItem(String title, IconData icon) {
    return ListTile(
      leading: Icon(icon),
      title: Text(title),
      trailing: const Icon(Icons.chevron_right),
      onTap: () {},
    );
  }
}

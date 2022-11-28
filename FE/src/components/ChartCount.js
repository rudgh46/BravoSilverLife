import React, { PureComponent } from "react";
import {
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";

export default class ChartCount extends PureComponent {
  static demoUrl = "https://codesandbox.io/s/simple-bar-chart-tpz8r";
  render() {
    const { info } = this.props;
    // console.log(this.props);
    const countData = [
      {
        name: "2018",
        "폐점 수": info?.terminatedCount.at(-5),
        "개점 수": info?.newCount.at(-5),
        amt: 2400
      },
      {
        name: "2019",
        "폐점 수": info?.terminatedCount.at(-4),
        "개점 수": info?.newCount.at(-5),
        amt: 2210
      },
      {
        name: "2020",
        "폐점 수": info?.terminatedCount.at(-3),
        "개점 수": info?.newCount.at(-5),
        amt: 2290
      },
      {
        name: "2021",
        "폐점 수": info?.terminatedCount.at(-2),
        "개점 수": info?.newCount.at(-5),
        amt: 2000
      },
      {
        name: "2022",
        "폐점 수": info?.terminatedCount.at(-1),
        "개점 수": info?.newCount.at(-5),
        amt: 2181
      }
    ];
    console.log(info);
    return (
      <ResponsiveContainer width="100%" height="100%">
        <BarChart
          width={500}
          height={300}
          data={countData}
          margin={{
            top: 5,
            right: 30,
            left: 20,
            bottom: 5
          }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis domain={[0, 500]} />
          <Tooltip />
          <Legend />
          <Bar dataKey="폐점 수" fill="#82ca9d" />
          <Bar dataKey="개점 수" fill="#8884d8" />
        </BarChart>
      </ResponsiveContainer>
    );
  }
}
